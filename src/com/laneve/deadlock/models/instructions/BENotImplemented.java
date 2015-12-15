package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;

public class BENotImplemented extends BEInstructionLine implements BEInstruction{

	public BENotImplemented(String text) {
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
	public void updateEnvironment(Environment environment) throws BEException{
		throw new BEException("Istruzione "+instructionName+" non gestita");
	}



}
