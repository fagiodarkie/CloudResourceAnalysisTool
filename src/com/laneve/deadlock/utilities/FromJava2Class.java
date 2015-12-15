package com.laneve.deadlock.utilities;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

// compila file .java per ottenere file .class
public class FromJava2Class {

	File sourceDir;
	File destDir;
	
	public FromJava2Class(String sourceDir, String destDir){
		this.sourceDir = new File(sourceDir);
		this.destDir = new File(destDir);
	}
	
	private void convert(File dir) throws IOException{
		
		try {
			File[] files = dir.listFiles();
			for (File fileEntry : files) {
				if (fileEntry.isDirectory()) { // e' una directory (nome di un package)
					convert(fileEntry);
				} else { // e' un file
					String filenameJ = fileEntry.toString().substring(fileEntry.toString().indexOf(sourceDir.toString())+(int)sourceDir.toString().length()+1);
			    	String filenameC = filenameJ.substring(0, filenameJ.lastIndexOf('.'));
			    	
			    	int extIndex = filenameJ.lastIndexOf('.');
					String ext = "";
					if (extIndex > 0) {
					    ext=filenameJ.substring(extIndex+1);
					}
					if(!ext.equals("java")){ //non e' un file .java --> lo salto
						continue;
					}
					
					
					System.out.println("Eseguo javac -d "+destDir.getAbsolutePath()+File.separator+" "+filenameJ);
					String[] cmd = {"javac","-cp",sourceDir.getAbsolutePath()+File.separator,"-d",destDir.getAbsolutePath()+File.separator,sourceDir.getAbsolutePath()+File.separator+filenameJ};
					try {
						
				    	System.out.println("Processing: "+ filenameJ);
				    	
						Process p = new ProcessBuilder(cmd).redirectOutput(Redirect.INHERIT).redirectError(Redirect.INHERIT).start();		
						
				    	System.out.println("Writing " + filenameC+".class");

						p.waitFor(); // attendo la terminazione del processo
						
				    	System.out.println("Writed " + filenameC+".class");
						
					} catch (IOException e) {
						System.err.println("Errore di I/O durante l'esecuzione del processo di \"javap\"");
						e.printStackTrace();
						System.exit(1);
					} catch (InterruptedException e) {
						System.err.println("Sono stato interrotto mentre attendevo che \"javap\" concludesse l'esecuzione");
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void convert() throws IOException{
		
		if(!destDir.exists()) //creo la directory di destinazione se non esiste
			destDir.mkdir();
		
		System.out.println("Reading on dir: "+ sourceDir.getAbsolutePath());
		System.out.println("Writing on dir: "+ destDir.getAbsolutePath());
		
		convert(sourceDir);
		
	}
}
