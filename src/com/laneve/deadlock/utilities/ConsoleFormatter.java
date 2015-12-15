package com.laneve.deadlock.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {
    //
    // Create a DateFormat to format the logger timestamp.
    //
    @SuppressWarnings("unused")
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
    
    @Override
    public String format(LogRecord record) {
    	return  record.getLevel() + ": " + record.getMessage() + "\n";
    }
 
    public String getHead(Handler h) {
        return super.getHead(h);
    }
 
    public String getTail(Handler h) {
        return super.getTail(h);
    }
}