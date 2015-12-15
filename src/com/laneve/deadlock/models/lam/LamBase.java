package com.laneve.deadlock.models.lam;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.Environment;
import com.laneve.deadlock.type.Type;
import com.laneve.deadlock.type.TypeObject;

public abstract class LamBase {
		
	public abstract String toString();
	
	// ritorna la lam semplificata
	public abstract String simplify();
	
	
	//ritorna la lista di tipi in Zbar
	public static LinkedList<TypeObject> getZbar(LinkedList<TypeObject> z){ 
		
		Set<TypeObject> set = new LinkedHashSet<TypeObject>();
		for(int i=0; i < z.size(); i++){
			set.add(z.get(i));
		}
		
		return z!=null && z.size()>0?new LinkedList<TypeObject>(set):null;
	}
	
	
	// ritorna il tipo in cima alla lista zbar o null altrimenti
	public static String getTopZbar(LinkedList<TypeObject> z){
		
		LinkedList<TypeObject> zbar= getZbar(z);
		return zbar!=null && zbar.size()>0?zbar.get(zbar.size()-1).getName():"0";		
	}

	
	public static LamAnd getZhat(LinkedList<TypeObject> z){
				
		ArrayList<LamSubExpr> lamSub = new ArrayList<LamSubExpr>();
		
		if(z!=null && z.size()>1){
			
			lamSub.add(new LamCouple(z.get(0).getName(), z.get(1).getName()));

			for(int i=1; i < z.size() -  1; i++){
								
				lamSub.add(new LamCouple(z.get(i).getName(),z.get(i+1).getName()));

			}
		}
		
		if(lamSub.isEmpty()) return new LamAnd(new LamZero());
		
		else return new LamAnd(lamSub);
		
	}
	
	
	public static LamAnd getZhatBar(LinkedList<TypeObject> z){

		return z!=null && z.size()>0?getZhat(getZbar(z)): new LamAnd(new LamZero());
	}
	
	
	/* Calcola T cappello */
	public static LamAnd getThat(LinkedList<TypeObject> t, Environment environment){
		
		ArrayList<LamSubExpr> lamSub = new ArrayList<LamSubExpr>();
		
		try{
			
			if(t!=null && t.size()>0){
				
				for(int i=0; i < t.size(); i++){
					ArrayList<Type> objects = new ArrayList<Type>();
					objects.add(t.get(i));
					
					//Se il metodo e' invocato su una classe che fa uso di campi statici esterni
					//bisogna passargli anche i campi statici per far si che questo li conosca se ne fa uso
					String clName=null,fieldName=null;
					for (Map.Entry<String, String> entry1 : environment.getFields().get(t.get(i).getClassName()).entrySet()){ //itero sui campi della classe
						if(entry1.getKey().contains(".")){ //il campo non e' di questa classe ma e' un riferimento ad un campo statico
							String fullfieldName=entry1.getKey(); //nomeClasseProprietaria.nomeCampo
							clName= fullfieldName.substring(0, fullfieldName.lastIndexOf('.')); //nomeClasseProprietaria
							if(environment.getClassObject(clName)==null) //la classe proprietaria non e' una classe user-defined
								continue;
							fieldName= fullfieldName.substring(fullfieldName.lastIndexOf('.')+1); //nome campo
							objects.add(environment.getClassObject(clName).getFieldType(fieldName));
						}
					}
					
					lamSub.add(new LamInvoke(t.get(i).getClassName(), "RUN",objects));
				}
			}
			
		}catch(BEException e){
			e.printStackTrace();
			System.exit(1);	
		}
		
		if(lamSub.isEmpty()) return new LamAnd(new LamZero());
		
		else return new LamAnd(lamSub);
	}
	
	
	/** pulisce una stringa LAM per renderla compatibile con i caratteri accettati dal tool DF4ABS */ 
	public static String cleanLamString(String lam){
		// problema: se ho metodi Pippo$1 e Pippo_1 e vado a pulire la stringa Pippo$1 facendola diventare Pippo_1
		// diventa uguale al secondo
		// soluzione: risolvo sostituendo '_' con '__' in modo che 
		// alla fine avro' Pippo_1 e Pippo__1 e riesco ancora a distinguerli
		
		lam= lam.replace("_", "__");
		lam= lam.replace("/", "_");
		lam= lam.replace("$", "_");
		lam= lam.replace("-", "_");
		lam= lam.replace("<", "_");
		lam= lam.replace(">", "_");
		lam= lam.replace(".", "_");
		lam= lam.replace("?", "_");
		lam= lam.replace(":", "_");
		lam= lam.replace("⊥", "_");
		lam= lam.replace("[", "_");
		lam= lam.replace("]", "_");
		
		return lam;
	}
}
