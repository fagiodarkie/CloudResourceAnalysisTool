package com.laneve.deadlock.utilities;

public final class Consts{

	/* true se deve generare i file .txt del bytecode a partire dai file .java delle classi user-defined */
	public static final boolean CONVERTJAVA2TXT = true;
	
	/* specifica la profondità di ricorsione sui campi nell'espansione dei tipi */
	public static int DEPTH = 2;
	
	/* specifica se i campi devono essere appiattiti
	 * se è false i campi degli vengono mostrati tra parentesi quadre 
	 * se e' true i campi vengono mostrati appiattiti cioe' a pari livello dell'oggetto che li contiene separati da virgole
	 * N.B: il tool DF4ABS e' compatibile solo con i campi appiattiti (FLATTEN = true) */
	public static final boolean FLATTEN=true; 
	
	
	private Consts(){
	    //non e' permesso istanziare questa classe
	    throw new AssertionError();
	  }
	

}
