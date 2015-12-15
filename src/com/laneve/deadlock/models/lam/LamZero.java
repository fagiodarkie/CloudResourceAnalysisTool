package com.laneve.deadlock.models.lam;

public class LamZero extends LamSubExpr {
	
	@Override
	public String toString(){
		return "0";
	}
	

	@Override
	public String simplify() {
		return toString(); //quindi "0"
	}
	
}
