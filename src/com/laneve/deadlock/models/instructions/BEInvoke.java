package com.laneve.deadlock.models.instructions;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import com.laneve.bytecode.parser.BytecodeParser.FormalParameterContext;
import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.BEConstantPool;
import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.BEMethodDeclaration;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;
import com.laneve.deadlock.models.lam.LamCouple;
import com.laneve.deadlock.models.lam.LamInvoke;
import com.laneve.deadlock.type.Type;
import com.laneve.deadlock.type.TypeInt;
import com.laneve.deadlock.type.TypeObject;

public class BEInvoke extends BEInstructionLine implements BEInstruction{
	
	private LamInvoke lamInv = null; //non e' presente per invocazione di metodi della standard library (tipo start e join)
	private LamCouple lamC=null; // la coppia di dipendenza tra chiamante e chiamato non e' sempre presente

	public BEInvoke(String text) {
		instructionName = text;
	}

	@Override
	public LamBase generateLam(Environment environment) {
				
		/* la coda dei thread puo' essere aggiornata in una start o una join quindi primo mi salvo la coda attuale
		 * per poi usarla nelle lam
		 */
		LinkedList<TypeObject> threads = new LinkedList<TypeObject>(environment.getQueuethreads());
				
		/* devo aggiornare prima l'environment così mentre faccio la pop dei parametri dallo stack 
		 * li aggiungo alla signature della mia LAM */
		updateEnvironment(environment); 
		
		LamAnd lamAnd =null;
			lamAnd =new LamAnd(LamBase.getZhatBar(environment.getLocks()), //zhhatbar
			LamBase.getThat(threads,environment), //that
			lamInv, //invocazione di metodo
			lamC); //dipendenza chiamante e chiamato
		
		return lamAnd;
	}

	@Override
	public void updateEnvironment(Environment environment) {
		String parameters="";
		int numParameters=0;
		ArrayList<Type> pars = new ArrayList<Type>();
		
		boolean privateMethod= false; 
		
		String s = BEConstantPool.takeCpoolRef(environment.getConstantPool(environment.getClassName()),getRef());
		String mName= s.substring(s.indexOf(" ")+1,s.lastIndexOf(" "));
		//System.out.println(mName);
		if(getName().contentEquals("invokespecial")  && !mName.equals("<init>")){ //invocazione di metodo privato della classe
			privateMethod=true;
		}
		
		
		if(getName().contentEquals("invokespecial") && !privateMethod){ //init
			
			//genera LAM
			String signature = BEConstantPool.takeCpoolRef(environment.getConstantPool(environment.getClassName()),getRef());
			//String methodName = signature.substring(signature.indexOf(" ")+1,signature.lastIndexOf(" ")); //nome del metodo
			String methodClass = signature.substring(signature.lastIndexOf(" ")+1); //la classe in cui si trova il costruttore	
			
			TypeObject ob=null;
			int openP = signature.indexOf("(");
			int closedP = signature.indexOf(")");
			parameters =  signature.substring(openP+1, closedP);
			numParameters=0;		
			for (int i = 0; i < parameters.length(); i++) {
				if (parameters.charAt(i) == 'L') { //e' tipo classe
					int index= parameters.indexOf(';',i);
					if(index!=-1){
						numParameters++;
						i=index;
						continue;
					}
				}
				if(parameters.charAt(i) == 'I' || parameters.charAt(i) == 'J'){
					numParameters++;
				}
			
			}
			try{
				for(int i = 0; i<numParameters; i++){
					Type type = environment.popStack(); 
					pars.add(0,type); // l'ultimo parametro va in cima perchè i parametri nello stack sono in ordine inverso
				}
	
				ob= (TypeObject) environment.popStack(); //il tipo dell'oggetto da inizializzare
				pars.add(0,ob);
			}
			catch(BEException e){
				e.printStackTrace();
				System.exit(1);
			}
			if(environment.getFields().containsKey(methodClass)){ //sto invocando un costruttore di una delle classi user-defined
				try {
					
					//vado a vedere dentro a questa init i putfield (che avvengono nell'ordine specificato dai parametri)
					// per settare correttamente i valori.
					
					//recupero i costruttori della classe
					ArrayList<BEMethodDeclaration> bmd= environment.getClassInitMethods(methodClass);
					
					BEMethodDeclaration initializer=null;
					
					//cerco il costruttore corrispondente (numero parametri e tipo devono coincidere)
					for(BEMethodDeclaration b: bmd){ //qui il ciclo e' solo su metodi init
						
						//l'array 'pars' contiene tutti i tipi dei parametri attuali + 
						// in prima posizione il this che non va considerarato nel controllo seguente
						
						ArrayList<FormalParameterContext> params = b.getMethodHeader().getMethodDeclarator().getFormalParameters();

						if(params.size() != (pars.size()-1)) continue; //non e' questo costruttore (numero parametri diverso)
						
						for(int i=0;i<params.size(); i++){
							if(!params.get(i).getText().equals(pars.get(i+1).getClassName())){ 
								//se il tipo di parametri è diverso non e' questo costruttore
								break; //guardane un altro
							}
						}
						
						//ho passato il for quindi il tipo di parametri erano uguali o non avevano parametri
						initializer=b;
					}
					

					if(initializer==null) throw new BEException("Il costruttore per la classe "+ methodClass +"non e' stato trovato"
							+ "\n. I parametri attuali non sono stati sostituiti correttamente");
					else{
						int k=1; //parte da 1 perche' in pars a 0 c'e' il this
						// ciclo sulle istruzioni
						String prevInst=null;
						for(BEInstructionLine inst: initializer.getMethodBody().getInstructions()){
							// quando trovo una putfield ottengo il nome del campo da settare
							if(inst.getName().equals("putfield")){
								
								if(prevInst.contains("load_"+k)){
									String getfieldRef = BEConstantPool.takeCpoolRef(environment.getConstantPool(methodClass),inst.getRef());
									String fieldName = getfieldRef.substring(getfieldRef.indexOf(" ")+1, getfieldRef.lastIndexOf(" ")); //nome del campo da modificare
									// aggiorno il field dell'oggetto su cui e' invocato il metodo con setFieldType e l'ultimo campo aggiunto		
									ob.setFieldType(fieldName, pars.get(k));
									k++;
								}
							
							}
							
							prevInst=inst.getName();
					
						}		
						
					}
					
					//Se il metodo e' invocato su una classe che fa uso di campi statici esterni
					//bisogna passargli anche i campi statici per far si che questo li conosca se ne fa uso
					String clName=null,fieldName=null;
					for (Map.Entry<String, String> entry1 : environment.getFields().get(methodClass).entrySet()){ //itero sui campi della classe
						if(entry1.getKey().contains(".")){ //il campo non e' di questa classe ma e' un riferimento ad un campo statico
							String fullfieldName=entry1.getKey(); //nomeClasseProprietaria.nomeCampo
							clName= fullfieldName.substring(0, fullfieldName.lastIndexOf('.')); //nomeClasseProprietaria
							if(environment.getClassObject(clName)==null) //la classe proprietaria non e' una classe user-defined
								continue;
							fieldName= fullfieldName.substring(fullfieldName.lastIndexOf('.')+1); //nome campo
							//String fieldType= entry1.getValue(); //tipo campo
							pars.add(environment.getClassObject(clName).getFieldType(fieldName));
						}
					}
					
					lamInv = new LamInvoke(methodClass,ob.getClassName(), pars);
				
				} catch (BEException e) {
					e.printStackTrace();
					System.exit(1);
				} 	
			}
	
		}else if(getName().contentEquals("invokevirtual") || privateMethod){ //invokevirtual o invokespecial su metodo privato

			String signature = BEConstantPool.takeCpoolRef(environment.getConstantPool(environment.getClassName()),getRef());
						
			if(signature.startsWith("()V start")){ //invokevirtual start

				try {
					environment.addThread((TypeObject)environment.popStack());
				} catch (BEException e) {
					e.printStackTrace();
					System.exit(1);
				}
			} //fine start

			else if(signature.startsWith("()V join")){ //invokevirtual join
				TypeObject tName=null;
				try {
					tName=(TypeObject) environment.popStack();
					environment.removeThread(tName);
				} catch (BEException e) {
					e.printStackTrace();
					System.exit(1);
				}

				lamC= new LamCouple(LamBase.getTopZbar(environment.getLocks()),tName.getName());

			} //fine join

			else{ //invokevirtual standard

				//genera LAM
				String methodClass = signature.substring(signature.lastIndexOf(" ")+1);
				String methodName = signature.substring(signature.indexOf(" ")+1,signature.lastIndexOf(" "));

				TypeObject ob=null;
				int openP = signature.indexOf("(");
				int closedP = signature.indexOf(")");
				parameters =  signature.substring(openP+1, closedP);
				numParameters=0;
				for (int i = 0; i < parameters.length(); i++) {
					if (parameters.charAt(i) == 'L') { //e' tipo classe
						int index= parameters.indexOf(';',i);
						if(index!=-1){
							numParameters++;
							i=index;
							continue;
						}
					}
					if(parameters.charAt(i) == 'I' || parameters.charAt(i) == 'J'){
						numParameters++;
					}
				
				}
				try{
					for(int i = 0; i<numParameters; i++){
						Type type = environment.popStack();
						pars.add(0,type);
					}
	
					ob= (TypeObject) environment.popStack(); // il tipo dell'oggetto su cui ho invocato il metodo
					pars.add(0,ob);

					if(environment.getFields().containsKey(methodClass)){ //non considero chiamate a metodi su classi non conosciute
						
						//Se il metodo e' invocato su una classe che fa uso di campi statici esterni
						//bisogna passargli anche i campi statici per far si che questo li conosca se ne fa uso
						String clName=null,fieldName=null;
						for (Map.Entry<String, String> entry1 : environment.getFields().get(methodClass).entrySet()){ //itero sui campi della classe
							if(entry1.getKey().contains(".")){ //il campo non e' di questa classe ma e' un riferimento ad un campo statico
								String fullfieldName=entry1.getKey(); //nomeClasseProprietaria.nomeCampo
								clName= fullfieldName.substring(0, fullfieldName.lastIndexOf('.')); //nomeClasseProprietaria
								if(environment.getClassObject(clName)==null) //la classe proprietaria non e' una classe user-defined
									continue;
								fieldName= fullfieldName.substring(fullfieldName.lastIndexOf('.')+1); //nome campo
								//String fieldType= entry1.getValue(); //tipo campo
								pars.add(environment.getClassObject(clName).getFieldType(fieldName));
							}
						}
						
						
						lamInv = new LamInvoke(ob.getClassName(), methodName, pars); 
						lamC= new LamCouple(LamBase.getTopZbar(environment.getLocks()),ob.getName());
					}
				}catch(BEException e){
					e.printStackTrace();
					System.exit(1);
				}

			}
			
			//devo aggiungere allo stack il tipo di ritorno (se c'e')
			int index = signature.indexOf(")");
			String resultType= signature.substring(index+1,index+2);
			
			if(resultType.equals("V") || resultType.equals("")){

				return; //tipo di ritorno void niente da aggiungere sullo stack
				
			}else if(resultType.equals("L")){

				String resultTypeClass = signature.substring(index+1,signature.indexOf(" "));

				if(resultTypeClass.lastIndexOf(';')!=-1){
					resultTypeClass= resultTypeClass.substring(1, resultTypeClass.lastIndexOf(';'));
				}

				environment.pushStack(new TypeObject(resultTypeClass,environment.getFields(),false));

			}
			else if(resultType.equals("I") || resultType.equals("F")){

				environment.pushStack(new TypeInt());
			}

		}
		else if(getName().contentEquals("invokestatic")){

			//genera LAM
			String signature = BEConstantPool.takeCpoolRef(environment.getConstantPool(environment.getClassName()),getRef());
			String methodClass = signature.substring(signature.lastIndexOf(" ")+1);
			String methodName = signature.substring(signature.indexOf(" ")+1,signature.lastIndexOf(" "));
			int openP = signature.indexOf("(");
			int closedP = signature.indexOf(")");
			parameters =  signature.substring(openP+1, closedP);
			numParameters=0;
			for (int i = 0; i < parameters.length(); i++) {
				if (parameters.charAt(i) == 'L') { //e' tipo classe
					int index= parameters.indexOf(';',i);
					if(index!=-1){
						numParameters++;
						i=index;
						continue;
					}
				}
				if(parameters.charAt(i) == 'I' || parameters.charAt(i) == 'J'){
					numParameters++;
				}
			
			}
			try{
				for(int i = 0; i<numParameters; i++){
					Type type = environment.popStack();
					pars.add(type);
				}
	
				// il tipo dell'oggetto su cui ho invocato il metodo e' la classe quindi non devo recuperare
				// alcun oggetto dallo stack
				
				if(environment.getFields().containsKey(methodClass)){ //non considero chiamate a metodi su classi non conosciute
					//Se il metodo e' invocato su una classe che fa uso di campi statici esterni
					//bisogna passargli anche i campi statici per far si che questo li conosca se ne fa uso
					String clName=null,fieldName=null;
					for (Map.Entry<String, String> entry1 : environment.getFields().get(methodClass).entrySet()){ //itero sui campi della classe
						if(entry1.getKey().contains(".")){ //il campo non e' di questa classe ma e' un riferimento ad un campo statico
							String fullfieldName=entry1.getKey(); //nomeClasseProprietaria.nomeCampo
							clName= fullfieldName.substring(0, fullfieldName.lastIndexOf('.')); //nomeClasseProprietaria
							if(environment.getClassObject(clName)==null) //la classe proprietaria non e' una classe user-defined
								continue;
							fieldName= fullfieldName.substring(fullfieldName.lastIndexOf('.')+1); //nome campo
							//String fieldType= entry1.getValue(); //tipo campo
							pars.add(environment.getClassObject(clName).getFieldType(fieldName));
						}
					}
					lamInv = new LamInvoke(methodClass,methodName, pars);
					lamC= new LamCouple(LamBase.getTopZbar(environment.getLocks()),environment.getClassObject(methodClass).getName());
				}
			}catch(BEException e){
				e.printStackTrace();
				System.exit(1);
			}
			//devo aggiungere allo stack il tipo di ritorno (se c'e')
			int index = signature.indexOf(")");
			String resultType= signature.substring(index+1,index+2);
			
			if(resultType.equals("V")){
	
				return; //tipo di ritorno void niente da aggiungere sullo stack
				
			}else if(resultType.equals("L")){ // tipo oggetto
	
				String resultTypeClass = signature.substring(index+1,signature.indexOf(" "));

				if(resultTypeClass.lastIndexOf(';')!=-1){
					resultTypeClass= resultTypeClass.substring(1, resultTypeClass.lastIndexOf(';'));
				}

	
				environment.pushStack(new TypeObject(resultTypeClass,environment.getFields(),false));
	
			}
			else if(resultType.equals("I") || resultType.equals("F")){ // tipo int o float
				// noi gestiamo solo int quindi creiamo un int anche per un float
				environment.pushStack(new TypeInt());
			}

		}

	}
}
