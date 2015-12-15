package com.laneve.deadlock.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.laneve.deadlock.models.lam.LamBase;
import com.laneve.deadlock.models.lam.LamClass;
import com.laneve.deadlock.models.lam.LamMethod;
import com.laneve.deadlock.exceptions.BEException;

public class BEClassFile extends BEBase{
	String className;
	
	BEConstantPool costantPool;
	LinkedHashMap<BEMethodHeader, BEMethodDeclaration> methods = new LinkedHashMap<BEMethodHeader, BEMethodDeclaration>();
	
	private static Logger LOGGER = Logger.getLogger("");

	public BEClassFile(String className,BEConstantPool costantPool) {
		super();
		// prendo il className in questo modo perchè la sua 
		// dichiarazione nella constantPool utilizza / dentro il nome
		// come separatore packageAndClassName invece di .
		// in questo modo il className è sempre lo stesso all'interno del programma
		this.className = className.replace(".", "/");
		this.costantPool = costantPool;
	}
	
	public void addMethod(BEMethodDeclaration methodDec) throws BEException {
		if(methods.containsKey(methodDec.getMethodHeader())){ //NON DOVREBBE SUCCEDERE
			throw new BEException("Non possono esserci due metodi con lo stesso header dentro lo stesso classfile");
		}
		methods.put(methodDec.getMethodHeader(), methodDec);
		
	}
	
	public String getClassName() {
		return className;
	}

	public BEConstantPool getCostantPool() {
		return costantPool;
	}
	
	public void removeMethod(BEMethodHeader key){
		methods.remove(key);
	}

	public void addMethodOnTop(BEMethodHeader bmh, BEMethodDeclaration bmd){
		
		LinkedHashMap<BEMethodHeader, BEMethodDeclaration> tmp= new LinkedHashMap<BEMethodHeader, BEMethodDeclaration>();
		
		tmp.put(bmh,bmd);
		
		for(Entry<BEMethodHeader, BEMethodDeclaration> m : methods.entrySet()){
			tmp.put(m.getKey(), m.getValue());
		}
		
		methods=tmp;
		
	}
	
	public LinkedHashMap<BEMethodHeader, BEMethodDeclaration> getMethods() {
		return methods;
	}
		
	@Override
	public LamBase generateLam(Environment environment) {
		ArrayList<LamMethod> lamTmp= new ArrayList<LamMethod>();
		for(Entry<BEMethodHeader, BEMethodDeclaration> m : methods.entrySet()){
			BEMethodDeclaration methodDeclaration = m.getValue();
			LamMethod lamMtd = (LamMethod) methodDeclaration.generateLam(environment); //ritorna una LamMethod
			lamTmp.add(lamMtd);
			
			//qundo siamo qui abbiamo già la LAM del nostro metodo
			
			LOGGER.info("-------------------------------------------------------------------------------\n");
			LOGGER.info(lamMtd.simplify()+"\n");
						
			LOGGER.info("-------------------------------------------------------------------------------\n");
			LOGGER.info(" FINE metodo "+ m.getKey().getMethodDeclarator().getMethodName() +"\t\t|\tclasse " +
						environment.getClassName()+"\n");
			LOGGER.info("-------------------------------------------------------------------------------\n");
		}
		
		LamClass lamClass=new LamClass(lamTmp);
		
		return lamClass; //ritona una LamClass
	}
}
