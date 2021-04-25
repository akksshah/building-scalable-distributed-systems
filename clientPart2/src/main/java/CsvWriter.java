import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import lombok.var;

public final class CsvWriter {
    private static final boolean MAKE_CSV = true;
    private static final DateFormat FORMATTER = new SimpleDateFormat("mm:ss.SSS");
    static {
        FORMATTER.setTimeZone(TimeZone.getTimeZone("EST"));
    }
    public static void writeToCsv(String fileName, List<RequestTracker> requests) throws FileNotFoundException {
        if (MAKE_CSV) {
            var csv = new File(fileName);
            var writer = new PrintWriter(csv);
            requests.stream().map(CsvWriter::convertToCsv).forEach(writer::println);
        } else {
            System.out.println("CSV was not produced");
        }
    }

    private static String convertToCsv(RequestTracker request) {
        return convertFrom(request.getStartTime()) + "," + convertFrom(request.getEndTime()) + "," + request.getLatency() + "," + request.getRequestType().name() + "," + (request.getResponseCode() == null ? "API_EXCEPTION" : request.getResponseCode());
    }

    private static String convertFrom(long ms) {
        return FORMATTER.format(new Date(ms));
    }
}
