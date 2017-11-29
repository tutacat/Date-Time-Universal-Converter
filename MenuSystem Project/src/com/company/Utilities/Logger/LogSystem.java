package com.company.Utilities.Logger;

import java.io.IOException;
import java.util.logging.*;

public class LogSystem
{
    private static Logger logger = Logger.getLogger("Application Log");
    private static FileHandler fh;

    private static boolean locked = false;

    private static SimpleFormatter formatter = new SimpleFormatter();

    public static void SetLogFile(String path) throws IOException {
        if(locked)
        {
            return;
        }

        fh = new FileHandler(path, 10000, 5, true);
        logger.addHandler(fh);

        logger.setUseParentHandlers(false);

        logger.info( "Log Active" );

        locked = true;
    }

    public static void WriteLog(String logLine){
        try {
            // This block configure the logger with handler and formatter
            fh.setFormatter(formatter);
            logger.info(logLine);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void LogError(String error){
        try {
            fh.setFormatter(formatter);
            logger.setLevel(Level.SEVERE);
            logger.severe(error);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void LogWarning(String warning){
        try{
            fh.setFormatter(formatter);
            logger.setLevel(Level.WARNING);
            logger.warning(warning);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}