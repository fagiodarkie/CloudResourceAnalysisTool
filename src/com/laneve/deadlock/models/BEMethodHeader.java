package com.laneve.deadlock.models;

import com.laneve.deadlock.models.lam.LamBase;

public class BEMethodHeader extends BEBase{
	String signature;
	String result;
	BEMethodDeclarator methodDeclarator;
	
	public BEMethodHeader(String result, BEMethodDeclarator methodDeclarator) {
		this.result = result;
		this.methodDeclarator = methodDeclarator;
		this.signature = result+" "+methodDeclarator.getMethodName();
	}
	
	public String getSignature(){
		return signature;
	}

	public String getResult() {
		return result;
	}

	public BEMethodDeclarator getMethodDeclarator() {
		return methodDeclarator;
	}

	@Override
	public LamBase generateLam(Environment environment) {
		return null;
	}

}
