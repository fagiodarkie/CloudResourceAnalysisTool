package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;

public class BEStore extends BEInstructionLine implements BEInstruction{
	private String localVarIndex;

	public BEStore(String text) {
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
		if(getName().contentEquals("astore")){
			localVarIndex = getNat();
		}else if(getName().contentEquals("istore")){
			localVarIndex = getNat();
		}else if(getName().contains("istore_")){
			localVarIndex = getName().substring(getName().indexOf("_")+1, getName().length());
		}else if(getName().contains("astore_")){
			localVarIndex = getName().substring(getName().indexOf("_")+1, getName().length());
		}

		//environment.popStack(): ora potrebbe non esserci nulla sullo stack ma a runtime se avviene un'eccezione 
		//sullo stack ci sara' un oggetto eccezione. Quindi staticamente
		//sullo stack non c'e' nulla e l'astore che gestisce l'eccezione tenta di prendere
		//qualcosa che c'e' solo a runtime e BOOM !

		//vedi se lo stack e' vuoto se e' questo il caso 
		//vedi se l'istruzione precedente e' un goto BEMEthodBody.getIstruzionePrecedente
		//prendi getNat() del goto e salta all'istruzione specificata dal goto
		try {
			
			if(environment.getOperandStack().isEmpty()){
				if(environment.getCurrentMethodBody().getInstructionTemp().getName().equals("goto")){
					String indexToJump = environment.getCurrentMethodBody().getInstructionTemp().getNat();
					Integer indexListTojump = environment.getCurrentMethodBody().getInstructionMap().get(indexToJump);
					environment.getCurrentMethodBody().setNextGotoInstruction(indexListTojump);
				}

			}else
				environment.putLocalVar(localVarIndex,environment.popStack());
			
		} catch (BEException e) {
			e.printStackTrace();
		}

	}
}
