package util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateBuilder {
    private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyyMMdd");
    public static Date getDate(String s) {
        try {
            return new Date(FORMATTER.parse(s).getTime());
        } catch (ParseException e) {
            return null;
        }
    }
}
