package zafus.rubikbmt.rubikbmt_website.IO;

import java.time.Duration;

public class ConvertToDuration {

    public static String convertToFormat(String input){
        return input.replaceAll("(?<=\\d)(?=(\\d{2})+$)", ".");
    }
    public static Duration parseDuration(String input) {
        // Tách các phần của chuỗi
        String[] parts = input.split("\\.");
        long millis = 0;
        long seconds = 0;
        long minutes = 0;
        long totalMillis = 0;
        if(parts.length == 2) {
            millis = Long.parseLong(parts[1]);
            seconds = Long.parseLong(parts[0]);
            totalMillis = (seconds * 1000) + (millis * 10);
        } else if(parts.length == 3) {
            millis = Long.parseLong(parts[2]);
            seconds = Long.parseLong(parts[1]);
            minutes = Long.parseLong(parts[0]);
            totalMillis = (minutes * 60 * 1000) + (seconds * 1000) + (millis * 10);
        }
        return Duration.ofMillis(totalMillis);
    }
}
