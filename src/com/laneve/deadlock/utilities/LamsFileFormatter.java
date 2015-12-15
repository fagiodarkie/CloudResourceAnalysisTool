package com.laneve.deadlock.utilities;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LamsFileFormatter extends Formatter {
    
    @Override
    public String format(LogRecord record) {
    	return  record.getMessage();
    }
 
    public String getHead(Handler h) {
        return super.getHead(h);
    }
 
    public String getTail(Handler h) {
        return super.getTail(h);
    }
}