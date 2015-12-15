package com.laneve.deadlock.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.lam.LamBase;
import com.laneve.deadlock.models.lam.LamInvoke;
import com.laneve.deadlock.models.lam.LamSequence;
import com.laneve.deadlock.models.lam.LamSubExpr;
import com.laneve.deadlock.type.Type;


public class BEMethodBody extends BEBase{
	LinkedList<BEInstructionLine> instructions;
	String methodModifier = null;
	BEMethodHeader methodHeader = null;
	HashMap<String, Integer> instructionMap;
	BEInstructionLine instructionTemp;
	Integer nextGoToInstruction = null;
	private LamInvoke lamInvokeSignature =null;

	private static Logger LOGGER = Logger.getLogger("");

	public BEMethodBody(LinkedList<BEInstructionLine> instructions,
			HashMap<String, Integer> instructionMap) {
		this.instructions = instructions;
		this.instructionMap = instructionMap;
	}

	public HashMap<String, Integer> getInstructionMap() {
		return instructionMap;
	}

	public LinkedList<BEInstructionLine> getInstructions() {
		return instructions;
	}

	public void setMethodSignature(String methodModifier,
			BEMethodHeader methodHeader) throws BEException{

		if(this.methodHeader!=null){
			throw new BEException("La signature di un methodBody non puo' essere modificata");
		}

		if(methodHeader==null){
			throw new BEException("La signature di un methodBody deve avere methodHeader");
		}

		this.methodModifier=methodModifier;
		this.methodHeader=methodHeader;
	}

	public BEMethodHeader getMethodHeader() throws BEException{
		return methodHeader;
	}
	
	public void setLamSignature(LamInvoke lamInv){
		
		this.lamInvokeSignature=lamInv;
	}
	
	public LamInvoke getLamSignature(){
		return this.lamInvokeSignature;
	}

	public String getMethodModifier() {
		return methodModifier;
	}

	public BEInstructionLine getInstructionTemp() {
		return instructionTemp;
	}
	
	public void setNextGotoInstruction(Integer indexListTojump){
			nextGoToInstruction = indexListTojump;
	}
	
	public Integer getNextGotoInstruction(){
		return nextGoToInstruction;
	}

	@Override
	public LamBase generateLam(Environment environment) {
		
		try {
			LOGGER.info("-------------------------------------------------------------------------------\n");
			LOGGER.info(" INIZIO metodo "+ getMethodHeader().getMethodDeclarator().getMethodName() +"\t|\tclasse " +
					environment.getClassName()+"\n");
			LOGGER.info("-------------------------------------------------------------------------------\n");
			
		} catch (BEException e) {
			e.printStackTrace();
		}
		
		ArrayList<LamSubExpr> lamArray = new ArrayList<LamSubExpr>();
		
		for(int i = 0 ; i < instructions.size() ;i++){			

			//salto per gestire il goto
			if(nextGoToInstruction != null){
				i = nextGoToInstruction;
				nextGoToInstruction = null;
			}
			
				LOGGER.info("----------------"+ instructions.get(i).getName() +"----------------\n");

				/* print Stack */
				String vars = "";
				for(Type obj : environment.getOperandStack()){
					vars = vars.concat(obj.getName() + ", ");
				}
				if(!vars.equals(""))
					vars = vars.substring(0, vars.length()-2);
				LOGGER.info("Stack:\t\t ["+vars+"]");
				  
				/* print LocalVar */
				Iterator<String> it = environment.getLocalVar().keySet().iterator();
				vars = "";
				while (it.hasNext()) {
				   String key = it.next().toString();
				   String value = environment.getLocalVar().get(key).getName();
				   vars = vars.concat(key + " -> " + value.toString() +", ");
				}
				if(!vars.equals(""))
					vars = vars.substring(0, vars.length()-2);
				LOGGER.info("LocalVar:\t\t ["+ vars+"]");
				
				/* print Locks */
				vars = "";
				for(Type obj : environment.getLocks()){
					vars = vars.concat(obj.getName() + ", ");
				}
				if(!vars.equals(""))
					vars = vars.substring(0, vars.length()-2);
				LOGGER.info("Locks:\t\t ["+vars+"]");
				
				/* print  queueThreads*/
				vars = "";
				for(Type obj : environment.getQueuethreads()){
					vars = vars.concat(obj.getName() + ", ");
				}
				if(!vars.equals(""))
					vars = vars.substring(0, vars.length()-2);
				LOGGER.info("QueueThreads:\t ["+vars+"]\n");
			
			/////////
				
			LamSubExpr lamSub= (LamSubExpr) instructions.get(i).generateLam(environment); //Ritorna una lam LamSubExpr
					
			lamArray.add(lamSub);
		
			////////
			
			/* print instruction's lam */
			LOGGER.info("Lam:\t\t ["+lamSub.toString()+"]\n");
			
			instructionTemp = instructions.get(i);

		}
		
		LamSequence l = new LamSequence(lamArray);

		return l; // ritorna una LamSequence
	}
}
