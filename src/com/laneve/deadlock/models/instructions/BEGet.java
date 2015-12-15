package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.BEConstantPool;
import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;
import com.laneve.deadlock.type.Type;
import com.laneve.deadlock.type.TypeInt;
import com.laneve.deadlock.type.TypeObject;

public class BEGet extends BEInstructionLine implements BEInstruction{

	public BEGet(String text) {
		instructionName = text;
	}

	@Override
	public LamBase generateLam(Environment environment) {
		LamAnd lamAnd =null;
		lamAnd =new LamAnd(LamBase.getZhatBar(environment.getLocks()), //zhhatbar
				LamBase.getThat(environment.getQueuethreads(),environment)); //tHat
		updateEnvironment(environment);
		return lamAnd;
	}

	@Override
	public void updateEnvironment(Environment environment){
		String getfieldRef = BEConstantPool.takeCpoolRef(environment.getConstantPool(environment.getClassName()),getRef());
		String fieldType = getfieldRef.substring(0, getfieldRef.indexOf(" ")); //tipo del campo da modificare
		if(fieldType.lastIndexOf(';')!=-1){
			fieldType= fieldType.substring(1, fieldType.lastIndexOf(';'));
		}
		String fieldName = getfieldRef.substring(getfieldRef.indexOf(" ")+1, getfieldRef.lastIndexOf(" ")); //nome del campo da modificare
		String fieldObjectType = getfieldRef.substring(getfieldRef.lastIndexOf(" ")+1); //tipo dell'oggetto in cui e' contenuto il campo
		String firstLetter = getfieldRef.substring(0, 1);

		if(getName().equals("getfield")){
			
			if(firstLetter.contentEquals("L")){ //tipo oggetto

				TypeObject o = null;
				try {
					o= (TypeObject) environment.popStack(); //tolgo dallo stack elemento da cui devo recuperare il campo
				} catch (BEException e) {
					e.printStackTrace();
					System.exit(1);
				}
				//fieldType = fieldType.substring(1); //rimuovi la L iniziale nella signature
				
				Type fType=o.getFieldType(fieldName);
				
				if(fType==null){
					// la profondita' data in input e' minore della profondita' di questo campo
					fType=new TypeObject(fieldType,environment.getFields(),false); //creo un oggetto fittizio per non mettere un null sullo stack
				}				
				
				environment.pushStack(fType);
			}
			else{ //sto recuperando un tipo intero
				try {
					environment.popStack(); //tolgo l'elemento da cui devo recuperare l'intero
				} catch (BEException e) {
					e.printStackTrace();
				}
				environment.pushStack(new TypeInt());
			}
			
		}else if(getName().equals("getstatic")){ //devo mettere sullo stack un campo di una classe
			if(firstLetter.contentEquals("L")){
				Type fType=null;
				if(environment.getClassObject(fieldObjectType) != null){ //e' una classe user-defined
					fType=environment.getClassObject(fieldObjectType).getFieldType(fieldName);
				}
				if(fType==null){
					//l'oggetto non fa parte di una classe user-defined
					//oppure la profondita' data in input e' minore della profondita' di questo campo
					fType=new TypeObject(fieldType,environment.getFields(),false); //creo un oggetto fittizio per non mettere un null sullo stack
				}
				environment.pushStack(fType);
			}
			else environment.pushStack(new TypeInt());
		}

	}

}
