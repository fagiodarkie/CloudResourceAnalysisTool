package com.laneve.deadlock.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.type.Type;
import com.laneve.deadlock.type.TypeObject;

public class Environment {
	
	//in comune a tutti i metodi di una classe
	String className;
	LinkedHashMap<String, BEConstantPool> cpools;
	BEConstantPool constantPool; //constantPoolAttuale
	private LinkedHashMap<String, LinkedHashMap<String, String>> fields;
	final LinkedHashMap<String, ArrayList<BEMethodDeclaration>> initMethods;
	private HashMap<String, TypeObject> classObjects;
	
	//per un singolo metodo
	LinkedList<Type>  operandStack; 
	LinkedList<TypeObject> locks, queueThreads;
	LinkedHashMap<String, Type> localVar;
	BEMethodBody currentMethodBody;	

	
	public Environment(LinkedHashMap<String, ArrayList<BEMethodDeclaration>> initMethods, LinkedHashMap<String, LinkedHashMap<String, String>> fields,
			LinkedHashMap<String, BEConstantPool> cpools, String className, HashMap<String, TypeObject> classObjects) {
		this.cpools= cpools;
		this.className = className;
		this.constantPool = cpools.get(className);	
		this.fields = fields;	
		this.initMethods= initMethods;
		this.classObjects = classObjects;
	}
	
	
	public TypeObject getClassObject(String className){
		return classObjects.get(className);
	}
		
	public LinkedHashMap<String, LinkedHashMap<String, String>> getFields() {
		return fields;
	}
	
	public String getClassName(){
		return className;
	}

	public BEMethodBody getCurrentMethodBody(){
		return currentMethodBody;
	}
	
	public ArrayList<BEMethodDeclaration> getClassInitMethods(String className) throws BEException{
		if(initMethods.get(className)==null){
			new BEException("Non si possono ottenere i costruttori di una classe non user-defined");
		}
		return initMethods.get(className);
	}

	public void openScope(BEMethodBody mb) {

		//inizializza le strutture
		operandStack = new LinkedList<Type>();
		locks = new LinkedList<TypeObject>();
		queueThreads = new LinkedList<TypeObject>();
		localVar = new LinkedHashMap<String, Type>();
		currentMethodBody=mb;

		//recupero i parametri dalla signature del metodo
		ArrayList<Type> pars= mb.getLamSignature().getParameters();
		
		boolean isStatic=false;
		
		TypeObject t= (TypeObject) pars.get(0); //il tipo dell'oggetto this o la classe se il metodo e' statico
		
		//setta i parametri nelle corrispondenti posizioni della localVar
		
		int j=0;
		
		if((!mb.getMethodModifier().equals("")) && 
				mb.getMethodModifier().contains("static")){ //ha un modificatore statico
			isStatic=true;
		}
		else{  
			localVar.put("0", t); //nella posizione 0 delle variabili locali c'e' il this se il metodo non e' statico
			j++;
		}

		
		for(int i=1;i<pars.size(); i++){
			localVar.put(String.valueOf(j),pars.get(i));
			j++;
		}
		
		//controllo se metodo synchronized
		if((!mb.getMethodModifier().equals("")) && 
				mb.getMethodModifier().contains("synchronized")){ 
			// se metodo e' synchronized  aggiungi il tipo dell'oggetto this o della classe (se e' statico) ai lock
			if(isStatic)
				addLock(t);
		}
		
	}
	
	public void closeScope() {
		operandStack = null;
		locks = null;
		queueThreads = null;
		currentMethodBody=null;
	}

	public BEConstantPool getConstantPool(String className) {
		return cpools.get(className);
	}

	public LinkedList<Type> getOperandStack() {
		return operandStack;
	}

	public LinkedList<TypeObject> getLocks() {
		return locks;
	}

	public void addLock(TypeObject lock){
		this.locks.add(lock);
	}

	public Type removeLock() throws BEException{
		Type o = null;
		if(!locks.isEmpty())
			o = locks.removeLast();
		//TODO scommentare quando gestite tutte le possibili istruzioni del bytecode
		//else throw new BEException("rimozione da pila dei lock vuota"); 
		return o;
	}

	public LinkedList<TypeObject> getQueuethreads() {
		return queueThreads;
	}

	public void addThread(TypeObject t){
		queueThreads.add(t);
	}

	public void removeThread(Type t){
		queueThreads.remove(t);
	}

	public HashMap<String, Type> getLocalVar() {
		return localVar;
	}


	//recupera il valore in cima allo stack senza rimuoverlo
	public Type getTopStack() throws BEException{
		Type o;
		if(!operandStack.isEmpty())
			o=operandStack.getLast();
		else throw new BEException("recupero valore da operand stack vuoto");

		return o;
	}

	//pop dall'operand stack e restituzione di tale oggetto
	public Type popStack() throws BEException {
		Type o = null;
		if(!operandStack.isEmpty())
			o = operandStack.removeLast();
		//TODO scommentare quando gestite tutte le possibili istruzioni del bytecode
		//else throw new BEException("pop da operand stack vuoto");
		return o;
	}
	
	//pop multiplo senza interesse su cosa si è scartato
	public void popStack(int count) throws BEException {

		for(int i = 0; i<count; i++){
			if(!operandStack.isEmpty())
				operandStack.removeLast();
			//TODO scommentare quando gestite tutte le possibili istruzioni del bytecode
			//else throw new BEException("pop da operand stack vuoto");
		}
	}
	public void pushStack(Type val){
		operandStack.add(val);
	}

	public void putLocalVar(String localVarIndex, Type val) {
		localVar.put(localVarIndex, val);

	}

}	
