package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;


public class BEAthrow extends BEInstructionLine implements BEInstruction{
	
	public BEAthrow(String text) {
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
		try {
			environment.popStack();
		} catch (BEException e) {
			e.printStackTrace();
		}
	}


}
