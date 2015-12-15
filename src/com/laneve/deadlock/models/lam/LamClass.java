package com.laneve.deadlock.models.lam;

import java.util.ArrayList;

public class LamClass extends LamBase{

	ArrayList<LamMethod> lamMtd= new ArrayList<LamMethod>(); 
	
	public LamClass(ArrayList<LamMethod> m){
		this.lamMtd = m;
	}

	@Override
	public String simplify() {
		String s="";
		for (LamMethod l : lamMtd) {
			   s+= l.simplify()+"\n";
		}
	    return s; 
	}
	
	@Override
	public String toString() {
		String s="";
		for (LamMethod l : lamMtd) {
		   s+= l+"\n";
		}
		return s;
	}

}

