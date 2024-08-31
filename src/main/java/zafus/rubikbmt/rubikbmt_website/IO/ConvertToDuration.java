package zafus.rubikbmt.rubikbmt_website.IO;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertToDuration {

    public static String convertToFormat(String input) {
        return input.replaceAll("(?<=\\d)(?=(\\d{2})+$)", ".");
    }

    public static Duration parseDuration(String input) {
        // Tách các phần của chuỗi
        String[] parts = input.split("\\.");
        long millis = 0;
        long seconds = 0;
        long minutes = 0;
        long totalMillis = 0;
        if (parts.length == 2) {
            millis = Long.parseLong(parts[1]);
            seconds = Long.parseLong(parts[0]);
            totalMillis = (seconds * 1000) + (millis * 10);
        } else if (parts.length == 3) {
            millis = Long.parseLong(parts[2]);
            seconds = Long.parseLong(parts[1]);
            minutes = Long.parseLong(parts[0]);
            totalMillis = (minutes * 60 * 1000) + (seconds * 1000) + (millis * 10);
        }
        return Duration.ofMillis(totalMillis);
    }

    public static String convertToNumber(String input) {
        Pattern pattern = Pattern.compile("PT(?:([0-9]+)M)?([0-9]+)(?:\\.([0-9]{1,2}))?S");
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            // Lấy phần phút, giây và thập phân
            String minutes = matcher.group(1); // Có thể là null
            String seconds = matcher.group(2);
            String fraction = matcher.group(3); // Phần thập phân (có thể là null)

            // Xử lý phần phút
            if (minutes == null) {
                minutes = ""; // Không có phần phút
            } else {
                // Đảm bảo phần phút không có chữ số 0 thừa
                minutes = String.format("%01d", Integer.parseInt(minutes));
            }

            // Xử lý phần giây
            if (seconds.length() == 1) {
                seconds = "0" + seconds; // Thêm số 0 nếu cần
            }

            // Xử lý phần thập phân
            if (fraction != null) {
                // Bỏ dấu chấm và đảm bảo có hai chữ số
                if (fraction.length() == 1) {
                    fraction += "0"; // Thêm số 0 nếu cần
                }
            } else {
                fraction = "00"; // Nếu không có phần thập phân, thêm "00"
            }

            // Kết hợp thành số nguyên duy nhất
            // Kiểm tra nếu phút bằng 0, chỉ hiển thị giây và mili giây
            if ("0".equals(minutes)) {
                return seconds + fraction;
            }

            // Kết hợp thành số nguyên duy nhất nếu có phút
            return minutes + seconds + fraction;
        }

        // Trả về null hoặc một giá trị mặc định nếu không khớp với định dạng
        return null;
    }
}
