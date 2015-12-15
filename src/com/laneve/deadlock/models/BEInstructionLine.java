package com.laneve.deadlock.models;

import com.laneve.deadlock.models.lam.LamBase;

public class BEInstructionLine extends BEBase {
	protected String instructionName;
	protected String number;
	protected String nat;
	protected String ref;
	protected String index;
	
	public String getName(){
		return instructionName;
	}
	
	public String getNumber() {
		return number;
	}

	public String getNat() {
		return nat;
	}
	
	public String getIndex() {
		return index;
	}
	
	public String getRef() {
		return ref;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setNat(String nat) {
		this.nat = nat;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public void setIndex(String index) {
		this.index = index;
	}

	@Override
	public LamBase generateLam(Environment environment) {return null;}

}
	