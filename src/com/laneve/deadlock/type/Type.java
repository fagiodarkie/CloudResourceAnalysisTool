package com.laneve.deadlock.type;


public abstract class Type {
	
	final String className; // nome base del tipo (senza indice)
	
	boolean isInt=false; 	// true per interi
	
	// se e' true il tipo identifica una classe
	// ne esiste uno per ogni classe del programma
	// non puo' essere indicizzato
	// i suoi campi invece possono essere settati/indicizzati
	boolean isClassType= false;
	
	public Type(String name){
		this.className = name;		
	}
	
	public boolean isInt(){
		return isInt; //se e' true il tipo e' intero
	}
	
	public String getClassName(){ //ottiene il nome semplice della classe del tipo (senza indice)
		return className;
	}

	public boolean isClassType(){
		return isClassType;
	};

	public abstract String getName(); //per la stampa del tipo non nella signature dei metodi
	
	public abstract String getNameForMethod(); // per la stampa del tipo nella signature dei metodi

	
}
