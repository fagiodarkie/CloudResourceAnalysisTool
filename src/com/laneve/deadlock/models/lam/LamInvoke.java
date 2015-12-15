package com.laneve.deadlock.models.lam;

import java.util.ArrayList;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.type.Type;

public class LamInvoke extends LamSubExpr {

	String className="";
	String methodName ="";
	ArrayList<Type> parameters = new ArrayList<Type>();
	
	public LamInvoke(String className, String methodName, Type...parameters) throws BEException{
		
		if(methodName==null || methodName.trim().isEmpty() || className==null || className.trim().isEmpty()){
			throw new BEException("Il nome del metodo e della classe deve essere valido");
		}
		this.className=className;
		this.methodName=methodName;
		for (Type p : parameters) { //TODO si possono rifiutare i tipi interi sin da subito
				this.parameters.add(p);
		    }
	}
	
	
	public LamInvoke(String className, String methodName, ArrayList<Type> parameters) throws BEException{
		
		if(methodName==null || methodName.trim().isEmpty() || className==null || className.trim().isEmpty()){
			throw new BEException("Il nome del metodo e della classe deve essere valido");
		}
		this.className=className;
		this.methodName=methodName;
		for (Type p : parameters) { //TODO si possono rifiutare i tipi interi sin da subito
				this.parameters.add(p);
		    }
	}

	
	public ArrayList<Type> getParameters(){
		return parameters;
	}

	@Override
	public String toString() {
		
		String mName=methodName;
		if(!methodName.contains(".") && !methodName.contains("/")){ // se il nome del metodo non è comprensivo di package...
			//...lo aggiungo per renderlo univoco
			mName=className+"."+mName;
		}
		
		String mSignature=mName.toUpperCase(); //metto il nome uppercase
		
		mSignature+="(";
				
		for(int i =0; i<parameters.size() ;i++){
		
			if(parameters.get(i).isInt()) //i tipi interi non li mettiamo nella signature dei metodi
				continue;
			else{
				mSignature+=parameters.get(i).getNameForMethod()+", ";
			}
		}
		if(mSignature.lastIndexOf(",")==mSignature.length()-2)
			mSignature=mSignature.substring(0, mSignature.lastIndexOf(","));
		
		mSignature+=")";
		
		return mSignature;
	}


	@Override
	public String simplify() {
		return toString(); //non c'è niente da semplificare
	}


}
