package Utils;

import Constants.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
/**
 * Utility class for logging different logs such as general logs, event logs, and query logs.
 */
public class QueryLogger {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Gets the current timestamp.
     *
     * @return the current timestamp as a string
     */
    private static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
    /**
     * Writes a log entry to the specified file.
     *
     * @param filePath the path of the file to write the log to
     * @param logEntry the log entry to be written
     */
    private static void writeLog(String filePath, String logEntry) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(logEntry + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Writes a general type log entry to the specified file.
     *
     * @param generalType the sub-type in general log
     * @param details the log entry to be written
     */
    public static void logGeneral(String generalType, String details) {
        String log = "{"
                + "\"timestamp\":\"" + getCurrentTimestamp() + "\","
                + "\"logType\":\"" + generalType + "\","
                + "\"details\":\"" + details.replace("\r", "").replace("\n", "") + "\""
                + "}";
        writeLog(Constants.GENERALLOGFILE, log);
    }

    /**
     * Writes a event type log entry to the specified file.
     *
     * @param eventType the sub-type in event log
     * @param description the log entry to be written
     */
    public static void logEvent(String eventType, String description) {
        String log = "{"
                + "\"timestamp\":\"" + getCurrentTimestamp() + "\","
                + "\"logType\":\"EVENT\","
                + "\"eventType\":\"" + eventType + "\","
                + "\"description\":\"" + description.replace("\r", "").replace("\n", "") + "\""
                + "}";
        writeLog(Constants.EVENTLOGFILE, log);
    }
    /**
     * Logs a query type log entry.
     *
     * @param userId the user ID associated with the query actioned by user
     * @param queryType the subtype of within log query
     * @param query the query details
     */
    public static void logQuery(String userId, String queryType, String query) {
        String log = "{"
                + "\"userId\":\"" + userId + "\","
                + "\"timestamp\":\"" + getCurrentTimestamp() + "\","
                + "\"queryType\":\"" + queryType + "\","
                + "\"query\":\"" + query.replace("\r", "").replace("\n", "") + "\""
                + "}";
        writeLog(Constants.QUERYLOGFILE, log);
    }
}