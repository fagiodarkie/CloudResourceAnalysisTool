package com.laneve.deadlock.models;

import java.util.ArrayList;

import com.laneve.bytecode.parser.BytecodeParser.FormalParameterContext;
import com.laneve.deadlock.models.lam.LamBase;

public class BEMethodDeclarator extends BEBase {
	String methodName;
	ArrayList<FormalParameterContext> formalParameters;
	
	public BEMethodDeclarator(String methodName, ArrayList<FormalParameterContext> formalParameters) {
		this.methodName = methodName;
		this.formalParameters = formalParameters;
	}
	public String getMethodName() {
		return methodName;
	}
	public ArrayList<FormalParameterContext> getFormalParameters() {
		return formalParameters;
	}
	@Override
	public LamBase generateLam(Environment environment) {
		return null;
	}
	
}
