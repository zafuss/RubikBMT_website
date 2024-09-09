package zafus.rubikbmt.rubikbmt_website.IO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeComponent {
    private int hours;
    private int minutes;
    private double seconds;

    public TimeComponent(int hours, int minutes, double seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public double getSeconds() {
        return seconds;
    }

    public static String printDuration(String duration) {
        TimeComponent timeComponent = parseISO8601Duration(duration);

        if (timeComponent != null) {
            String formattedMinutes = timeComponent.getMinutes() < 10 ? "0" + timeComponent.getMinutes() : String.valueOf(timeComponent.getMinutes());
            String formattedSeconds = timeComponent.getSeconds() < 10 ? "0" + timeComponent.getSeconds() : String.valueOf(timeComponent.getSeconds());

            // Kiểm tra nếu formattedSeconds có dạng xx.x và thêm số 0
            if (formattedSeconds.matches("\\d+\\.\\d")) {
                formattedSeconds += "0";
            }

            if (timeComponent.getHours() > 0) {
                return timeComponent.getHours() + ":" + formattedMinutes + ":" + formattedSeconds;
            }
            if (formattedMinutes.equals("00")) {
                return formattedSeconds;
            }
            return formattedMinutes + ":" + formattedSeconds;
        } else {
            return "Invalid ISO 8601 duration format";
        }
    }

    public static TimeComponent parseISO8601Duration(String iso8601Duration) {
        // Biểu thức chính quy để phân tích định dạng ISO 8601
        String regex = "PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+(?:\\.\\d+)?)S)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(iso8601Duration);

        if (matcher.matches()) {
            // Trích xuất giờ, phút và giây
            String hoursStr = matcher.group(1);
            String minutesStr = matcher.group(2);
            String secondsStr = matcher.group(3);

            int hours = (hoursStr != null) ? Integer.parseInt(hoursStr) : 0;
            int minutes = (minutesStr != null) ? Integer.parseInt(minutesStr) : 0;
            double seconds = (secondsStr != null) ? Double.parseDouble(secondsStr) : 0.0;

            return new TimeComponent(hours, minutes, seconds);
        }
        return null;
    }

    public Double convertToTime() {
        return hours * 3600 + minutes * 60 + seconds;
    }
}
