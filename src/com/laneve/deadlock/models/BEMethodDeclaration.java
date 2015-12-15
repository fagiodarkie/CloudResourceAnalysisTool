package com.laneve.deadlock.models;

import java.util.ArrayList;
import java.util.Map;

import com.laneve.bytecode.parser.BytecodeParser.FormalParameterContext;
import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.lam.LamBase;
import com.laneve.deadlock.models.lam.LamInvoke;
import com.laneve.deadlock.models.lam.LamMethod;
import com.laneve.deadlock.models.lam.LamSequence;
import com.laneve.deadlock.type.Type;
import com.laneve.deadlock.type.TypeInt;
import com.laneve.deadlock.type.TypeObject;


public class BEMethodDeclaration extends BEBase{
	String modifier;
	BEMethodHeader methodHeader;
	BEMethodBody methodBody;
		
	public BEMethodDeclaration(String modifier,BEMethodHeader methodHeader,BEMethodBody methodBody) {
		this.modifier = modifier;
		this.methodHeader = methodHeader;
		this.methodBody = methodBody;
	}

	public String getModifier() {
		return modifier;
	}

	public BEMethodHeader getMethodHeader() {
		return methodHeader;
	}

	public BEMethodBody getMethodBody() {
		return methodBody;
	}
	
	BEMethodDeclaration getMethodDeclaration(){
		return this;
	}

	@Override
	public LamBase generateLam(Environment environment) {
						
		//LAM signature del metodo
		
		ArrayList<Type> aType= new ArrayList<Type>();
		
		String className=environment.getClassName();
		
		TypeObject t=new TypeObject(className,environment.getFields(),false); //il this del metodo (solo per metodi non statici)
				
		TypeObject tClass= environment.getClassObject(className);//l'oggetto corrispondente alla classe (se il metodo è statico)
		
		//recupero il nome del metodo
		String methodName = getMethodHeader().getMethodDeclarator().getMethodName();
					
		ArrayList<FormalParameterContext> pars = getMethodHeader().getMethodDeclarator().getFormalParameters();
		
		if((!methodBody.getMethodModifier().equals("")) && 
				!methodBody.getMethodModifier().contains("static")){ //non ha un modificatore statico
			aType.add(t);
		}
		else{
			aType.add(tClass);
		}

		for(int i=0;i<pars.size(); i++){
			Type t1=null;
			if(pars.get(i).getText().equals("int")){
				t1=new TypeInt();
			}
			else{
				t1= new TypeObject(pars.get(i).getText().replace('.', '/'),environment.getFields(),false);
			}
			
			aType.add(t1);
		}
		
		//Se il metodo e' invocato su una classe che fa uso di campi statici esterni
		//bisogna passargli anche i campi statici per far si che questo li conosca se ne fa uso
		String clName=null,fieldName=null;
		for (Map.Entry<String, String> entry1 : environment.getFields().get(className).entrySet()){ //itero sui campi della classe
			if(entry1.getKey().contains(".")){ //il campo non e' di questa classe ma e' un riferimento ad un campo statico
				String fullfieldName=entry1.getKey(); //nomeClasseProprietaria.nomeCampo
				clName= fullfieldName.substring(0, fullfieldName.lastIndexOf('.')); //nomeClasseProprietaria
				if(environment.getClassObject(clName)==null) //la classe proprietaria non e' una classe user-defined
					continue;
				fieldName= fullfieldName.substring(fullfieldName.lastIndexOf('.')+1); //nome campo
				//String fieldType= entry1.getValue(); //tipo campo
				aType.add(environment.getClassObject(clName).getFieldType(fieldName));
			}
		}
		
		//creo la signature per la LAM del metodo
		LamInvoke lamInvk=null;
		try {
			lamInvk = new LamInvoke(environment.getClassName(),methodName,aType);
		} catch (BEException e) {
			e.printStackTrace();
			System.exit(1);
		}

		methodBody.setLamSignature(lamInvk);
		
		environment.openScope(this.methodBody);
		LamSequence lamSeq = (LamSequence) methodBody.generateLam(environment); //ritorna una LamSequence
		LamMethod lamM= new LamMethod(lamInvk,lamSeq);
		environment.closeScope();
		return lamM; //ritorna una LamMethod
	}

}
