package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.models.BEConstantPool;
import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;
import com.laneve.deadlock.type.TypeObject;

public class BENew extends BEInstructionLine implements BEInstruction{

	public BENew(String text) {
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
		//creo un nuovo oggetto
		String className= BEConstantPool.takeCpoolRef(environment.getConstantPool(environment.getClassName()), getRef());
		environment.pushStack(new TypeObject(className,environment.getFields(),false));
				
	}
}
