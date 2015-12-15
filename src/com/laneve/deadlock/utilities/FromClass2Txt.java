package com.laneve.deadlock.utilities;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

// converte file .class in bytecode usando il comando javap
public class FromClass2Txt {
	
	File sourceDir;
	File destDir;
	
	public FromClass2Txt(String sourceDir, String destDir){
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
					
					String filenameC = fileEntry.toString().substring(fileEntry.toString().indexOf(sourceDir.toString())+(int)sourceDir.toString().length()+1);
			    	String filenameT = filenameC.substring(0, filenameC.lastIndexOf('.')) + ".txt";
			    	
			    	File txtFile = new File(destDir.getAbsolutePath() + File.separator+ filenameT);
			    	txtFile.getParentFile().mkdirs();
			    	
			    	int extIndex = filenameC.lastIndexOf('.');
					String ext = "";
					if (extIndex > 0) {
					    ext=filenameC.substring(extIndex+1);
					}
					if(!ext.equals("class")){ //non e' un file .class --> lo salto
						continue;
					}
			    		    	
					System.out.println("Eseguo javap -c -v -s -private "+ filenameC);
					String[] cmd = {"javap","-c", "-v", "-s", "-private", sourceDir.getAbsolutePath()+File.separator+filenameC};

					try {
						
				    	System.out.println("Processing: "+ filenameC);

						Process p = new ProcessBuilder(cmd).redirectError(Redirect.INHERIT).redirectOutput(
								txtFile).start();		
						
				    	System.out.println("Writing " + filenameT);
				    	
						p.waitFor(); // attendo la terminazione del processo
						
				    	System.out.println("Writed " + filenameT);
						
					} catch (IOException e) {
						System.err.println("Errore di I/O durante l'esecuzione del processo \"javap\"");
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
