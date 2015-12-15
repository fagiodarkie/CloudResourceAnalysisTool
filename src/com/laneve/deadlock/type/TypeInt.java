package com.laneve.deadlock.type;

/** Tipo intero */
public class TypeInt extends Type {

	//costruttore per il tipo degli interi
	public TypeInt(){
		super("int"); // particolare classe int
		isInt=true; //e' un tipo intero
	}

	@Override
	public String getName() {
		return className; //cioe' int
	}

	@Override
	public String getNameForMethod() {
		return className;
	}

	
}
