package com.laneve.deadlock.models;

import com.laneve.deadlock.models.lam.LamBase;


public class BETableEntry extends BEBase{
	String ref;
	BEConstantAndInfo costantAndInfo;
	
	public BETableEntry(String ref, BEConstantAndInfo costantAndInfo) {
		this.ref = ref;
		this.costantAndInfo = costantAndInfo;
	}

	public String getRef() {
		return ref;
	}

	public BEConstantAndInfo getCostantAndInfo() {
		return costantAndInfo;
	}

	@Override
	public LamBase generateLam(Environment environment) {
		return null;
	}
	
}
