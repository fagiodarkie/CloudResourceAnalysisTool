package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.models.lam.LamAnd;
import com.laneve.deadlock.models.lam.LamBase;

public class BEIf extends BEInstructionLine implements BEInstruction{

	public BEIf(String text) {
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
		
		/* TODO da implementare correttamente cioe'
		*  far proseguire al thread corrente l'esecuzione del blocco if
		*  e creare un altro thread che esegua in parallelo il blocco else;
		*  A questo secondo thread andra' passata una copia dell'environment attuale in modo
		*  che possa partire dai corretti operand stack, variabili locali, coda dei lock, ecc..
		*  
		*  TODO prima di fare quanto scritto sopra c'e' un problema da risolvere:
		*  se dentro a questo if c'e' un goto ad esempio di un ciclo while
		*  come distinguo la fine del blocco if da quel goto ?
		*/

		if(getName().contentEquals("cmp")){
			try {
				environment.popStack(2);
			} catch (BEException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}else{
			try {
				environment.popStack();
			} catch (BEException e) {
				e.printStackTrace();
				System.exit(1);	
			}
		}
	}

}
