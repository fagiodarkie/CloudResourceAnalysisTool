package com.laneve.deadlock.type;

import java.util.LinkedHashMap;
import java.util.Map;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.utilities.Consts;


/** Tipo oggetto */
public class TypeObject extends Type{
	
	/* campo usato per generare i tipi fresh */
	static private int indexCounter = 1;
	
	Integer index = null; //indice (null se tipo non ha index)

	TypeObject parent = null; //sono un tipo senza tipo padre
	
	//ogni tipo oggetto ha un insieme di campi che hanno un nome e un tipo
	LinkedHashMap <String, Type> fieldsRecord=null;
	
	// costruttore di default per i tipi oggetto
	// il primo parametro e' il nome della classe del tipo da creare (senza indice, preso dalla constantPool al momento della new)
	// il secondo parametro è la mappa che conosce i campi per i tipi definiti nelle classi bytecode
	// il terzo parametro specifica se e' un tipo classe (non puo' essere indicizzato) oppure no
	public TypeObject(String className, LinkedHashMap<String,LinkedHashMap<String,String>> fields, boolean isClassType){
		super(className);
		fieldsRecord = new LinkedHashMap <String,Type>();
		this.isClassType=isClassType?true:false;
		try { //setto il suo indice
			assignIndex();
		} catch (BEException e) { // non si puo' mai verificare, il tipo e' appena stato creato --> non ha ancora un indice
			e.printStackTrace();
		}
		setFieldsRecursively(this, fields, Consts.DEPTH);

	}
	
	/** Crea un oggetto per la costante null (usato con istruzione aconst_null) */
	public TypeObject(){
		super("NULL");
		fieldsRecord = new LinkedHashMap <String,Type>(); //evita problemi di null pointer (il controllo avviene con isEmpty())
	}
	
	
	// ritorna la classe base del tipo + l'indice assegnato
	// il nome del campo a cui e' associato non viene stampato
	String getIndexName() {
		String s=this.className;
		if(index!=null)
			s+= "_"+index;
		return s;
	}

	//recupera il tipo di un campo
	public Type getFieldType(String fieldName) {
		return fieldsRecord.get(fieldName);
	}
	
	//recupera la mappa dei campi di questo oggetto
	private LinkedHashMap <String,Type> getFieldsRecord(){
		return fieldsRecord;
	}
	
	
	//restituisce l'oggetto con l'indice appena assegnato
	void assignIndex() throws BEException{
		if(this.index!=null) throw new BEException("Non puoi riassegnare un indice ad un tipo");
		else{
			if(!isClassType) // i tipi classe non possono avere un indice associato (ma si possono indicizzare i loro campi)
				this.index=TypeObject.getFreshIndex();
		}
	}
	
	@Override
	public String getNameForMethod(){ // per la stampa del tipo nella signature dei metodi
		if(Consts.FLATTEN){ //voglio i campi appiattiti per la compatibilita' con il tool DF4ABS
			return flattenedFields(this);
		}else{ //campi non appiattiti (mostrati tra parentesi quadre)
			return typeAndFields(this); //...e di seguito i suoi campi tra parentesi quadre
		}
	}
	
	@Override
	public String getName(){ //per la stampa del tipo NON nella signature dei metodi
		return getIndexName();
	}
	
	
	String getFlattenName() {
		return getName();
	}
	
	String getRecordName() {
		return getName();
	}
	
	
	//controlli per garantire unicita' del tipo all'interno della mappa e per fare comparazioni
	
	public boolean equals(Object obj) {
		if (!(obj instanceof TypeObject))
			return false;	
		if (obj == this)
			return true;
		return this.getIndexName().equals(((TypeObject) obj).getIndexName());
	}
 
	public int hashCode(){
		return getIndexName().hashCode();
	}
	
	
	// ritorna un indice fresh
	static int getFreshIndex(){
		return indexCounter++;
	}
	
	/*************************************/
	
	/* costruttore utilizzato solo con funzione setFieldsRecursively*/
	TypeObject(String className,LinkedHashMap<String,LinkedHashMap<String,String>> fields,int depth){
		super(className);
		fieldsRecord = new LinkedHashMap <String,Type>();
		setFieldsRecursively(this, fields, depth-1);
	}
	
	// setta tutti i tipi dei campi di quest'oggetto ad un tipo fittizio oppure a null (non inizializzati)
	// a seconda del valore initializedFields
	private static void setFieldsRecursively(TypeObject startType, final LinkedHashMap<String,LinkedHashMap<String,String>> fields, int depth){
		
		if (depth==0) return; //termina ricorsione

		if(fields.get(startType.getClassName())!=null){ //abbiamo la classe nella nostra mappa
			//recupero i tipi dei campi
			LinkedHashMap<String,String> f = fields.get(startType.getClassName());
			LinkedHashMap<String,Type> typeFields = new LinkedHashMap <String,Type>();
		    for(Map.Entry<String, String> entry : f.entrySet()){
		    	//entry.getValue() e' il tipo del campo e entry.getKey() e' il suo nome
				if(entry.getValue().equals("int")) typeFields.put(entry.getKey(),new TypeInt()); //interi considerati sempre inizializzati
				else{ //campo oggetto
					TypeObjectField t=null;
					String fieldName= entry.getKey();
					//viene creato un nuovo campo con un nuovo indice
					t = new TypeObjectField(entry.getValue(), fields, depth);
					try {
						t.assignIndex(); //indice fittizio
					} catch (BEException e) {
						e.printStackTrace();
						System.exit(1);
					} 
						
					try {
						t.setFieldName(fieldName);
					} catch (BEException e) {
						e.printStackTrace();
						System.exit(1);
					}
					t.parent=startType; //salvo il tipo del genitore di questo campo
					typeFields.put(fieldName, t);
				}
			}

			startType.fieldsRecord=typeFields; // tra i campi vi sono salvati anche quelli di tipo int
		}
		
	}
	
	
	// ritorna la stringa che rappresenta il tipo t comprensivo dei campi visitati ricorsivamente
		public static String typeAndFields(TypeObject t){
				
				String s = t.getName();
				if(!t.getFieldsRecord().isEmpty()){ //se questo tipo ha campi
					s+="["; //apro la parentesi per elencarli
					for(Map.Entry<String, Type> entry : t.getFieldsRecord().entrySet()){
						//String fieldName=entry.getKey(); //nome campo
						if(entry.getValue()!=null && entry.getValue().isInt()) continue; //evito di stampare i campi int
						else{	// e' un TypeObjectField
							s+=typeAndFields(((TypeObject)entry.getValue()))+", " ;	 // vado ricorsivamente a prendere il suo tipo
						}
					}
					if(s.lastIndexOf(",")==s.length()-2)
						s= s.substring(0, s.lastIndexOf(",")); //rimuovo l'ultima virgola
					s+="]";
				}
				
				return s;
		}
		
		
		// ritorna una stringa che rappresenta la vista appiattita dei campi dell'oggetto di tipo t
		public static String flattenedFields(TypeObject t){		
								
				String s = t.getName(); //prendo nome appiattito del tipo corrente (es a)
				if(!t.getFieldsRecord().isEmpty()){ //se questo tipo ha campi
					s+=", "; //lo separo con la virgola
					for(Map.Entry<String, Type> entry : t.getFieldsRecord().entrySet()){ //scorro sulla mappa dei campi che ho creato durante l'istanziazione del tipo
						if(entry.getValue()!=null && entry.getValue().isInt()) continue; //evito di stampare i campi int
						else{ //se il campo e' di tipo oggetto vado a vedere ricorsivamente i suoi campi
							//a meno che non sia un tipo di cun campo statico
							if(!entry.getKey().contains(".")){
								s+= flattenedFields(((TypeObject)entry.getValue()))+", " ;	
							}
						}
					}
				}
				if(s.lastIndexOf(",")==s.length()-2)
					s= s.substring(0, s.lastIndexOf(",")); //rimuovo l'ultima virgola
				return s;
		}

		
		public void setFieldType(String fieldName, Type type) {
			
			if(type instanceof TypeObject){
				((TypeObject) type).parent = this;
			}
			fieldsRecord.put(fieldName, type);
		}


		
}
