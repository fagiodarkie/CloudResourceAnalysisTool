package com.laneve.deadlock.models;

import java.util.ArrayList;

import com.laneve.deadlock.models.lam.LamBase;


public class BEConstantAndInfo extends BEBase{
	
	ArrayList<String> constantAndInfo; 
	
	public BEConstantAndInfo(ArrayList<String> constantAndInfo) {
		this.constantAndInfo = constantAndInfo;
	}

	public ArrayList<String> getConstantAndInfo() {
		return constantAndInfo;
	}

	@Override
	public LamBase generateLam(Environment environment) {
		return null;
	}

}
