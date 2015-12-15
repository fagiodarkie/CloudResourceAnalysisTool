package com.laneve.deadlock;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.laneve.bytecode.parser.BytecodeLexer;
import com.laneve.bytecode.parser.BytecodeParser;
import com.laneve.deadlock.models.BEClassFile;
import com.laneve.deadlock.models.BEConstantAndInfo;
import com.laneve.deadlock.models.BEConstantPool;
import com.laneve.deadlock.models.BEMethodDeclaration;
import com.laneve.deadlock.models.BEMethodDeclarator;
import com.laneve.deadlock.models.BEMethodHeader;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamBase;
import com.laneve.deadlock.models.lam.LamClass;
import com.laneve.deadlock.type.TypeObject;
import com.laneve.deadlock.utilities.ConsoleFormatter;
import com.laneve.deadlock.utilities.Consts;
import com.laneve.deadlock.utilities.FileUtils;
import com.laneve.deadlock.utilities.FromClass2Txt;
import com.laneve.deadlock.utilities.FromJava2Class;
import com.laneve.deadlock.utilities.LamsFileFormatter;
import com.laneve.deadlock.visitor.BytecodeVisitor;

public class DeadlockAnalysis {
	
	private static Logger FILELOGGER = Logger.getLogger("lams_log");

	public static void main(String[] args) throws IOException{
				
		File inputDir=null;
		File outputDir=null;
		
		if(args.length<1 || args.length>5){ //non va bene
			System.err.println("Usage: DeadlockAnalysis inputFolder [-o outputFolder] [-d maxSearchDepth]");
			System.exit(1);
		}
		else{
			for (int i = 0; i < args.length; i++) {
		        switch (args[i].charAt(0)) {
		        case '-': //opzione
		        	 if(args[i].equals("-o") && (i+1)<args.length){ //il parametro seguente specifica la cartella di output

		        		 String folderPath=args[i+1];
		        		 if(args[i+1].toString().charAt((int)args[i+1].toString().length()-1)==File.separatorChar){
		        			 folderPath.substring(0, folderPath.length()-1);
		        		 }
		        		 outputDir= new File(args[i+1]);
		        	 }
		        	 else if(args[i].equals("-d") && (i+1)<args.length){ //il parametro seguente specifica la profondita' di ricerca
		        		 try{
		        			 int depth=Integer.valueOf(args[i+1]);
		        			 if(depth<0) throw new NumberFormatException();
		        			 else Consts.DEPTH=depth;
		        		 }catch(NumberFormatException nfe){
		        			 System.err.println("L'opzione -d deve essere seguita da un intero positivo");
		        			 System.exit(1);
		        		 }
		        	 }
		        	 else{
		        		System.err.println("L'opzione "+args[i]+" non e' corretta o manca del suo argomento"); 
		    			System.err.println("Usage: DeadlockAnalysis inputFolder [-o outputFolder] [-d maxSearchDepth]");
		        		System.exit(1);
		        	 }
		        	 i++;
		        	 break;
		        default:
		        	if(inputDir!=null){ // si e' gia' settata la cartella di input
		        		System.err.println("Come cartella di input e' gia' stata settata la cartella "+inputDir+"."
		        				+ "Il parametro "+args[i]+" e' errato");
		        		System.exit(1);
		        	}
		        	inputDir = new File(args[i]);
		        	if(!inputDir.exists()){
		        		System.err.println("La cartella di input \""+inputDir+"\" non esiste");
		        		System.exit(1);
		        	}
		        	break;
		        }
			}
		}
		
		if(outputDir==null){ //la cartella di output non e' stata settata, ne metto una di default
			outputDir = new File("output_DeadlockAnalysis");
		}
		
		if(outputDir.exists()){ 
			// se questa cartella esiste gia' devo rimuovere tutto il suo contenuto	
			// per evitare problemi derivanti da output precedenti 	
			FileUtils.deleteFolder(outputDir);
		}
		outputDir.mkdir(); //creo la cartella di output
		

		/*** log configuration ***/
		Logger rootLog = Logger.getLogger("");
		rootLog.setLevel(Level.ALL);

		Handler handler = rootLog.getHandlers()[0];
		handler.setFormatter(new ConsoleFormatter());
		
		FILELOGGER.setLevel(Level.INFO);
		FileHandler hand;
		
		hand = new FileHandler(outputDir+File.separator+"lams.txt");
		hand.setFormatter(new LamsFileFormatter());
		FILELOGGER.setUseParentHandlers(false);
		FILELOGGER.addHandler(hand);		
		
		/* Converte i file di input .java in class e da questi ottiene il bytecode in file .txt */
		if(Consts.CONVERTJAVA2TXT){
			FromJava2Class fj2c = new FromJava2Class(inputDir.toString(), outputDir+File.separator+"classFiles");
			fj2c.convert();
			FromClass2Txt fc2t = new FromClass2Txt(outputDir+File.separator+"classFiles", outputDir+File.separator+"bytecode");
			fc2t.convert(); 
		}
		
		File folder = new File(outputDir+File.separator+"bytecode"); //cartella in cui e' contenuto il nostro bytecode
		ArrayList<LamBase> lams = new ArrayList<LamBase>(); //insieme delle Lam
		Environment environment;
		LinkedHashMap<String, LinkedHashMap<String, String>> fields = new LinkedHashMap<String, LinkedHashMap<String,String>>(); 

		ArrayList<BEClassFile> classfiles = new ArrayList<BEClassFile>();
		generateParseTreeAndVisitIt(folder, classfiles);
				
		boolean trovato=false; //true quando ho trovato il metodo main
		int cIndex=0;
		BEMethodHeader mKey=null; 
		
		//posiziono il main in cima ai metodi per la generazione corretta delle Lam per il tool DF4ABS
		for(int i=0; i<classfiles.size(); i++){
			for (Map.Entry<BEMethodHeader, BEMethodDeclaration> entry : classfiles.get(i).getMethods().entrySet()){
				if(entry.getKey().getMethodDeclarator().getMethodName().equals("main")){
					if(trovato){
						System.err.println("Non possono esistere 2 metodi main()");
						System.exit(1);
					}
					else{ 
						trovato=true;
						cIndex=i;
						mKey=entry.getKey();
					}
					
				}
			}
		}
		
		if(!trovato){ //all'interno delle classi .java non e' stato trovato alcun metodo main
			System.err.println("Deve esistere almeno un metodo main().");
			System.exit(1);
		}
		else{
			BEClassFile tmp = classfiles.get(cIndex); //prendo il ClassFile che contiene il main
			BEMethodDeclaration bmd = tmp.getMethods().get(mKey); //prendo il metodo main
			tmp.removeMethod(mKey); //rimuovo il metodo dalla posizione in cui si trovava
			tmp.addMethodOnTop(mKey, bmd); //lo metto in cima alla lista dei metodi
			classfiles.remove(tmp); //rimuovo il vecchio classfile dalla lista dei classfile
			classfiles.add(0, tmp); //e aggiungo l'elemento modificato in cima
		}

		for(BEClassFile cf : classfiles){ //creiamo dalla constant Pool la mappa <NomeClasse, <nomeCampo, tipoCampo>>
			String className="",nameAndType="",fieldName = "",type="";
			LinkedHashMap<String, String> fieldNameAndTypes = new LinkedHashMap<String, String>();
			HashMap<String, BEConstantAndInfo> tableEntries = cf.getCostantPool().getTableEntries();
			for (Map.Entry<String, BEConstantAndInfo> entry : tableEntries.entrySet()){
				ArrayList<String> a = entry.getValue().getConstantAndInfo();
				if(a.get(0).contentEquals("Fieldref")){
					boolean staticField = false;
					className = BEConstantPool.takeCpoolRef(cf.getCostantPool(), a.get(1));
					nameAndType = BEConstantPool.takeCpoolRef(cf.getCostantPool(), a.get(2));
					type = nameAndType.substring(0, nameAndType.indexOf(" "));
					fieldName = nameAndType.substring(nameAndType.lastIndexOf(" ")+1);
					
					if(!className.equals(cf.getClassName())){ //ho un riferimento a un campo statico di un altra classe (esempio campo out di java/io/PrintStream)						
						staticField = true;
					}
					
					if(type.trim().startsWith("L")){ //oggetto
						type=type.substring(1,type.length()-1);
						if(staticField) fieldName= className+"."+fieldName;
						fieldNameAndTypes.put(fieldName, type);
					}
					else{ //intero
						if(staticField) fieldName= className+"."+fieldName;
						fieldNameAndTypes.put(fieldName, "int");
					}
				}
			}
			fields.put(cf.getClassName(), fieldNameAndTypes);
		}
		
		
		LinkedHashMap<String, ArrayList<BEMethodDeclaration>> initMethods = new LinkedHashMap<String, ArrayList<BEMethodDeclaration>>();
		
		for(BEClassFile cf : classfiles){ //itero sulle classi
			ArrayList<BEMethodDeclaration> m = new ArrayList<BEMethodDeclaration>(); //tutti i costruttori per questa classe
			for (Map.Entry<BEMethodHeader, BEMethodDeclaration> entry : cf.getMethods().entrySet()){ //itero sui metodi della classe	
				BEMethodDeclarator bmd = entry.getKey().getMethodDeclarator();
				if(bmd.getMethodName().equals(cf.getClassName())){
					m.add(entry.getValue());
				}
			}
			initMethods.put(cf.getClassName(),m);
		}
		
		//creo gli oggetti per ogni classe, devono essere in comune per tutte le classi
		HashMap<String, TypeObject> classObjects= new HashMap<String, TypeObject>();
		for (Map.Entry<String, LinkedHashMap<String, String>> entry : fields.entrySet()){
			String key = entry.getKey();
			TypeObject t = new TypeObject(key,fields,true);
			if(classObjects.containsKey(key)){
				System.err.println("Ci sono due classi con lo stesso nome nella mappa fields");
				System.exit(1);
			}
			classObjects.put(entry.getKey(), t);
		}
				
		LinkedHashMap<String, BEConstantPool> cpools = new LinkedHashMap<String, BEConstantPool>();
		for(BEClassFile cf : classfiles){
			cpools.put(cf.getClassName(), cf.getCostantPool());
		}
		
		for(BEClassFile cf : classfiles){
			environment = new Environment(initMethods, fields , cpools,cf.getClassName(),classObjects);
			LamClass lam= (LamClass) cf.generateLam(environment); //contiene le Lam per tutti i metodi
			lams.add(lam); //lams alla fine sara' un insieme di LamClass
			
			// semplifica la lam rimuovendo comportamenti zero e duplicati
			String s = lam.simplify().toString();
			
			//creo il file delle lam per il tool DF4ABS			
			String cleanedLam = LamBase.cleanLamString(s);
			if(Consts.FLATTEN){
				FILELOGGER.info(cleanedLam+"\n");
			}else FILELOGGER.info(s+"\n");
			
		}		
		
	}
	
	// va ricorsivamente alla ricerca dei file .txt del bytecode di ogni classe dentro alla cartella bytecode
	// per creare i parse tree e visitarli
	// restituisce l'array di BEClassFile (1 BEClassFile per ogni classe)
	private static void generateParseTreeAndVisitIt(File folder, ArrayList<BEClassFile> classfiles) {
	    File[] files = folder.listFiles();
	    if(files!=null) {
	        for(File fileEntry: files) {
	            if(fileEntry.isDirectory()) { //e' una dirctory devo andare ricorsivamente all'interno
	            	generateParseTreeAndVisitIt(fileEntry, classfiles);
	            } else { //e' un file bytecode, vado a generare e visitare il parse tree 
	        		FileInputStream in = null;
					try {
						in = new FileInputStream(fileEntry);
					} catch (FileNotFoundException e1) {
						System.err.println("Il file "+fileEntry+"non e' stato trovato");
						e1.printStackTrace();
						System.exit(1);
					}
	        		ANTLRInputStream input=null;
					try {
						input = new ANTLRInputStream(in);
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(1);
					}
	        		BytecodeLexer lexer = new BytecodeLexer(input);
	        		CommonTokenStream tokens = new CommonTokenStream(lexer);
	        		BytecodeParser parser = new BytecodeParser(tokens);
	        		ParseTree tree = parser.classfile();

	        		BytecodeVisitor bcVisitor = new BytecodeVisitor(); //visitor
	        		classfiles.add((BEClassFile) bcVisitor.visit(tree)); 
	            }
	        }
	    }	    
	}
	
}
