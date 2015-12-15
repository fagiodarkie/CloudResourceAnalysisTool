package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;

public class BELoad extends BEInstructionLine implements BEInstruction{
	private String localVarIndex;

	public BELoad(String text) {
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
		if(getName().contentEquals("aload")){
			localVarIndex = getNat();
		}else if(getName().contentEquals("iload")){
			localVarIndex = getNat();
		}else if(getName().contains("iload_")){
			localVarIndex = getName().substring(getName().indexOf("_")+1, getName().length());
		}else if(getName().contains("aload_")){
			localVarIndex = getName().substring(getName().indexOf("_")+1, getName().length());
		}
		
		environment.pushStack(environment.getLocalVar().get(localVarIndex)); //il tipo nelle variabili locali e' già indicizzato
	}
}
