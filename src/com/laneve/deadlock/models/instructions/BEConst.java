package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.models.BEConstantPool;
import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;
import com.laneve.deadlock.type.TypeInt;
import com.laneve.deadlock.type.TypeObject;

public class BEConst extends BEInstructionLine implements BEInstruction{
	
	public BEConst(String text) {
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
		
		if(getName().contains("iconst_") || getName().equals("bipush")){
			environment.pushStack(new TypeInt());
		}else if(getName().equals("aconst_null")){
			environment.pushStack(new TypeObject());
		}else if(getName().contains("ldc")){
			String cpType = BEConstantPool.takeCpoolRefType(environment.getConstantPool(environment.getClassName()),getRef());
			if(cpType.equals("Integer") || cpType.contains("Long"))
				environment.pushStack(new TypeInt());
			else{
				String obType=BEConstantPool.takeCpoolRef(environment.getConstantPool(environment.getClassName()),getRef());
				if(obType.lastIndexOf(';')!=-1){
					obType= obType.substring(1, obType.lastIndexOf(';'));
				}
				environment.pushStack(new TypeObject(obType,environment.getFields(),false));
			}
		}
	}
	
}
