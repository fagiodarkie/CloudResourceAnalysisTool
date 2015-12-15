package com.laneve.deadlock.type;

import java.util.LinkedHashMap;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.utilities.Consts;


public class TypeObjectField extends TypeObject{

	private String fieldName = null; //il nome del campo

	public TypeObjectField(String className, String fieldName, LinkedHashMap<String,LinkedHashMap<String,String>> fields, TypeObject parent){
		super(className,fields,false);
		this.parent = parent; //sono un campo, ho un tipo padre
		this.fieldName = fieldName;
	}
	
	/* costruttore utilizzato solo con funzione TypeObject.setFieldsRecursively*/
	TypeObjectField(String className,LinkedHashMap<String,LinkedHashMap<String,String>> fields,int depth){
		super(className, fields,depth);
	}
	
	
	//setta il nome campo del tipo se non e' gia' stato settato
	public void setFieldName(String fieldName) throws BEException{
		if(this.fieldName!=null) throw new BEException("Il nome del campo non puo' essere riassegnato");
		this.fieldName = fieldName;
	}
	
	
	// ritorna il nome del tipo comprensivo di indice e prefissato con i tipi indicizzati dei genitori
	@Override 
	String getFlattenName(){
		
		String s="_"+fieldName+"_";

		s+= getIndexName();
		
		TypeObject p= parent;
		
		s=p.getFlattenName()+s; //tipi indicizzati dei padri + _nomeCampo_ + tipoCampo
		
		return s;
		
	}
	
	@Override
	String getRecordName() { //per la stampa dei record non appiattiti
		
		String s=fieldName+":";

		s+= getIndexName();
		
		//TypeObject p= parent;
		
		//s=p.getRecordName()+s; //tipi indicizzati dei padri + nomeCampo + : + tipoCampo
		
		return s;
	}
	
	@Override
	public String getName() { //per la stampa del tipo NON nella signature dei metodi
		if(Consts.FLATTEN){
			return getFlattenName();
		}
		else{
			return getRecordName();
		}
	}

	public TypeObject getParent() {
		return parent;
	}

	public String getFieldName() {
		return fieldName;
	}

		
}
