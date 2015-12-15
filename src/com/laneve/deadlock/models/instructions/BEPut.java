package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.BEConstantPool;
import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;

public class BEPut extends BEInstructionLine implements BEInstruction{

	public BEPut(String text) {
		instructionName = text;
	}

	@Override
	public LamBase generateLam(Environment environment) {
		LamAnd lamAnd =null;
		lamAnd =new LamAnd(LamBase.getZhatBar(environment.getLocks()), //zhhatbar
				LamBase.getThat(environment.getQueuethreads(),environment)); //tHat
		try {
			updateEnvironment(environment);
		} catch (BEException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return lamAnd;
	}

	@Override
	public void updateEnvironment(Environment environment) throws BEException {
		
		String getfieldRef = BEConstantPool.takeCpoolRef(environment.getConstantPool(environment.getClassName()),getRef());
		//String fieldType = getfieldRef.substring(0, getfieldRef.indexOf(" ")); //tipo del campo da modificare
		//String fieldName = getfieldRef.substring(getfieldRef.indexOf(" ")+1, getfieldRef.lastIndexOf(" ")); //nome del campo da modificare
		String fieldObjectType = getfieldRef.substring(getfieldRef.lastIndexOf(" ")+1); //tipo dell'oggetto in cui e' contenuto il campo
		if(getName().equals("putfield")){
			
			//non si puo' modificare il campo della classe se non siamo nella sua init
			if(!environment.getCurrentMethodBody().getMethodHeader().getMethodDeclarator().getMethodName().equals(environment.getClassName())){
				
				throw new BEException("Non si possono modificare campi gia' inizializzati");
			}  //else sono in un costruttore e posso settare il valore del campo
			
		}else if(getName().equals("putstatic")){
			//non si può modificare il campo statico se non siamo nel blocco statico della classe
			
			if(!(environment.getClassName().equals(fieldObjectType) && 
					environment.getCurrentMethodBody().getMethodHeader().getMethodDeclarator().getMethodName().contains("staticBlock?"))){ 
				throw new BEException("Non si possono modificare campi statici al di fuori del loro blocco statico");
			}
			try {
				environment.popStack();
			} catch (BEException e) {
				e.printStackTrace();
				System.exit(1);
			}	
		}	
	}
}
