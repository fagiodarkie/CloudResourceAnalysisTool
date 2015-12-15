package com.laneve.deadlock.models.lam;

public class LamMethod extends LamBase {

	LamInvoke lamInv; //signature del metodo
	LamSequence lamSeq;
	
	public LamMethod(LamInvoke lamInv, LamSequence lamSeq){
		this.lamInv=lamInv;
		this.lamSeq=lamSeq;
	}
	
	public LamInvoke getLamInvoke(){
		return lamInv;
	}
	
	public LamSequence getLamSeq(){
		return lamSeq;
	}
	
	@Override
	public String toString() {
		return lamInv.toString()+" = "+lamSeq.toString();
	}

	@Override
	public String simplify() {
		return lamInv.simplify()+" = "+lamSeq.simplify();
	}

}
