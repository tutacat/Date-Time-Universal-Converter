package com.company.Utilities.Logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogSystem
{
    private static Logger logger = Logger.getLogger("Application Log");
    private static FileHandler fh;

    private static boolean locked = false;

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
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info(logLine);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}