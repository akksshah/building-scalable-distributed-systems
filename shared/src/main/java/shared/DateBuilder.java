package shared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateBuilder {
    private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyyMMdd");
    public static Date getDate(String s) {
        try {
            return FORMATTER.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }
}
