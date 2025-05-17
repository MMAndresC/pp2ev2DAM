package com.svalero.musicrxjava.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    public static String formatFromString(String stringDate, String outFormat, String inPattern) throws DateTimeParseException {
        try {
            //Convert to LocalDate to validate date
            DateTimeFormatter inFormat = DateTimeFormatter.ofPattern(inPattern);
            LocalDate date = LocalDate.parse(stringDate, inFormat);

            // Change it to new format
            DateTimeFormatter changedFormat = DateTimeFormatter.ofPattern(outFormat);
            return date.format(changedFormat);
        }catch(DateTimeParseException e){
            return "";
        }
    }
}
