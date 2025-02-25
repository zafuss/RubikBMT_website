package zafus.rubikbmt.rubikbmt_website.utilities;

public class PasswordGenerator {
    public static String generatePassword(String email, String phoneNumber) {
        if (email == null || phoneNumber == null || !email.contains("@") || phoneNumber.length() < 3) {
            throw new IllegalArgumentException("Email hoặc số điện thoại không hợp lệ");
        }

        // Lấy phần trước dấu "@" và giữ lại ký tự "@"
        int atIndex = email.indexOf("@");
        String prefix = email.substring(0, atIndex + 1);

        // Viết hoa chữ cái đầu tiên
        prefix = Character.toUpperCase(prefix.charAt(0)) + prefix.substring(1);

        // Lấy 3 số cuối của số điện thoại
        String lastThreeDigits = phoneNumber.substring(phoneNumber.length() - 3);

        // Kết hợp thành mật khẩu
        return prefix + lastThreeDigits;
    }
}