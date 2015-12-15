package com.laneve.deadlock.models.lam;

public class LamCouple extends LamSubExpr{
	
	String first="";
	
	String second="";
	
	public LamCouple(String first, String second){
		this.first=first;
		this.second=second;
	}
	
	@Override
	public String toString(){

		return "("+first+", "+second+")";
		
	}

	@Override
	public String simplify() {
		if(first==null || first.isEmpty() || first.trim().equals("0") || 
				second==null || second.isEmpty() || second.trim().equals("0")){
			return "0";
		}
		else return toString();
	}
	
}
