package com.laneve.deadlock.utilities;

import java.io.File;

public class FileUtils {

	// rimuovi tutto il contenuto di una cartella andando a rimuovere ricorsivamente il contenuto delle sottocartelle
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) {
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
}
