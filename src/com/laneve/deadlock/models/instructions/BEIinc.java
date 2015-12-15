package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;
import com.laneve.deadlock.type.TypeInt;

public class BEIinc extends BEInstructionLine implements BEInstruction{

	
	public BEIinc(String text) {
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
	public void updateEnvironment(Environment environment) {
		
		String localVarIndex = getNat();
		
		/* e' inutile ottenere il valore da incrementare...tanto alla fine il
		 * il risultato sara' sempre un tipo int
		 */
		//String inc = getNumber(); 
		
		environment.putLocalVar(localVarIndex, new TypeInt());
		
	}

}
