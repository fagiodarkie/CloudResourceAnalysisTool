package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;

public class BEGoto extends BEInstructionLine implements BEInstruction{

	public BEGoto(String text) {
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
		//TODO bisogna capire come gestire il goto
		// puo' essere il goto di un if-else o di un ciclo while, o di uno switch, o proprio di un'istruzione goto
		// se while è dentro if oppure if e' dentro while come riconosco di chi e' il goto?
		
	}


}
