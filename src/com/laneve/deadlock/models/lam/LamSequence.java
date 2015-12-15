package com.laneve.deadlock.models.lam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/** Alla fine la Lam di un metodo sara' una LamSequence*/
public class LamSequence extends LamBase {
	
	private ArrayList<LamSubExpr> lamSeq = new ArrayList<LamSubExpr>();
	
	public LamSequence(LamSubExpr...lamsub){
		
	    for (LamSubExpr l : lamsub) { //TODO si possono rifiutare le LamZero da subito
	    	lamSeq.add(l);
	    }
	}

	public LamSequence(ArrayList<LamSubExpr> lamsub){
		
	    for (LamSubExpr l : lamsub) { //TODO si possono rifiutare le LamZero da subito
	    	lamSeq.add(l);
	    }
	}

	@Override
	public String simplify() {
		
		/* chiamo un semplify ricorsivamente su tutte le mie lam ed elimino quelle uguali */
				
		Set<String> sSet= new HashSet<String>();
						
		ArrayList<LamSubExpr> tmp=new ArrayList<LamSubExpr>();	
		
		
		for(int i=0; i<lamSeq.size();i++){
			LamSubExpr lsub = lamSeq.get(i);
			String simplified = lsub.simplify();
			if(!simplified.equals("0") && !sSet.contains(simplified)){
				tmp.add(lsub);
				sSet.add(simplified);
			}
		}
		
		lamSeq=tmp; //LamSequence semplificata in maniera distruttiva
		
		String s = toString();
				
		return s;
	}

	@Override
	public String toString() {

		String s="";
		if(lamSeq.size() > 0){
			for (LamSubExpr l : lamSeq) {
		        s+=l.toString() + "; ";
		    }
			if(s.lastIndexOf(";")==s.length()-2) // l'ultimo ';' non ci deve essere 
				s=s.substring(0, s.lastIndexOf(";"));
		}else s="0";

		return s;
		
	}


}
