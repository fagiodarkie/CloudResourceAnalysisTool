package com.laneve.deadlock.models;

import java.util.ArrayList;
import java.util.HashMap;

import com.laneve.deadlock.models.lam.LamBase;


public class BEConstantPool extends BEBase{
	HashMap<String, BEConstantAndInfo> tableEntries =  new HashMap<String, BEConstantAndInfo>();

	public void addTableEntry(BETableEntry tableEntry) {
		this.tableEntries.put(tableEntry.getRef(), tableEntry.costantAndInfo);
	}

	public HashMap<String, BEConstantAndInfo> getTableEntries() {
		return tableEntries;
	}
	
	
	/*
	 * ------------
	 * Entry della constant pool
	 * -------------
	 * 'Class' ref 
	| 'Fieldref' ref'.'ref
	| 'Methodref' ref'.'ref
	| 'InterfaceMethodref' ref'.'ref
	|  STRING ref
	| 'Integer' num
	| 'Float' DEC
	| 'Long' num'l'
	| 'Double' DEC
	| 'NameAndType' ref':'ref
	|
	 */
	public static String takeCpoolRef(BEConstantPool constantPool,String ref) {
		BEConstantAndInfo constantInfo = constantPool.tableEntries.get(ref);
		ArrayList<String> a = constantInfo.getConstantAndInfo();
		switch (a.get(0)) {
		case "Class":
			String cRef = a.get(1);
			return takeCpoolRef(constantPool,cRef);
		case "Methodref":
			String mRef = a.get(1);
			takeCpoolRef(constantPool,mRef);
		case "Fieldref":
			String fRef = a.get(1);
			String nameAndTypeRef = a.get(2);
			String classnameField = takeCpoolRef(constantPool,fRef);
			String nameAndType = takeCpoolRef(constantPool,nameAndTypeRef);
			//return classnameField+'.'+nameAdnType;
			return nameAndType + " " + classnameField;
		case "NameAndType":
			String methodNameRef = a.get(1);
			String returnTypeRef = a.get(2);
			String methodName = takeCpoolRef(constantPool,methodNameRef);
			String returnType = takeCpoolRef(constantPool,returnTypeRef);
			returnType = returnType.substring(0, returnType.length());
			return returnType+" "+methodName;
		case "String":
			return "java/lang/String";
		default://"Utf8"
			return a.get(1);
		}
	}
	
	
	public static String takeCpoolRefType(BEConstantPool constantPool, String ref) {
		BEConstantAndInfo constantInfo = constantPool.tableEntries.get(ref);
		ArrayList<String> a = constantInfo.getConstantAndInfo();
		return a.get(0);
	}

	@Override
	public LamBase generateLam(Environment environment) {
		return null;
	}

}
