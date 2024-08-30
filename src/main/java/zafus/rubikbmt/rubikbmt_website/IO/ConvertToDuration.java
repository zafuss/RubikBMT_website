package zafus.rubikbmt.rubikbmt_website.IO;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String convertToNumber(String input) {
        // Regex để trích xuất số phút và số giây
        Pattern pattern = Pattern.compile("PT(\\d+)M(\\d+)(\\.\\d{1,2})?S");
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            // Lấy phần phút và giây
            String minutes = matcher.group(1);
            String seconds = matcher.group(2);
            String fraction = matcher.group(3); // phần thập phân (có thể là null)

            // Xử lý phần thập phân
            if (fraction != null) {
                fraction = fraction.substring(1); // Bỏ dấu chấm
                if (fraction.length() == 1) {
                    fraction += "0"; // Thêm số 0 nếu cần
                }
            } else {
                fraction = "00"; // Nếu không có phần thập phân, thêm "00"
            }

            // Kết hợp thành số nguyên duy nhất
            return minutes + seconds + fraction;
        }

        return "";
    }
}
