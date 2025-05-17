package com.svalero.musicrxjava.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class ErrorLogger {
    private static final String MAIN_DIRECTORY = "musicApi";
    private static final String LOGS_DIRECTORY = "logs";
    private static  final String FILE_ERROR_LOG = "errors.log";
    private static final Logger errorLogger = Logger.getLogger(ErrorLogger.class.getName());

    static {
        try{
            String separator = File.separator;
            String path = System.getProperty("user.home") + separator + MAIN_DIRECTORY + separator + LOGS_DIRECTORY;

            File logDir = new File(path);
            //Create directory
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            FileHandler errorHandler = new FileHandler(path + separator + FILE_ERROR_LOG, true);
            errorHandler.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(LogRecord record) {
                    return String.format("%1$tF %1$tT - %2$s%n", record.getMillis(), record.getMessage());
                }
            });
            errorLogger.addHandler(errorHandler);
            //no show in console
            errorLogger.setUseParentHandlers(false);
        }catch(IOException e){
            System.err.println("Error al configurar el logger: " + e.getMessage());
        }
    }

    public static void log(String error) {
        errorLogger.log(Level.SEVERE, error);
    }
}
