package zafus.rubikbmt.rubikbmt_website.requestEntities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import zafus.rubikbmt.rubikbmt_website.entities.Role;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidEmail;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidPhoneNumber;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidUserName;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestCreateTeacher {
    @ValidUserName
    @Size(min = 1, max = 50, message = "Tên Tài Khoản phải có từ 1 đến 50 ký tự")
    private String userName;
    @Pattern(regexp =  "^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#@$%&? \"]).*$", message = "Mặt khẩu không hợp lệ")
    private String passwordHash;
    private String repasswordHash;
    @Size(min = 1, max = 50, message = "Email phải có từ 1 đến 50 ký tự")
    @Email(message = "Không đúng định dạng Email")
    @ValidEmail
    private String email;
    @Length(min = 10, max = 10, message = "Phone phải có 10 số")
    @Pattern(regexp = "^[0-9]*$", message = "Phone phải là số")
    @ValidPhoneNumber
    private String phoneNumber;
    private LocalDateTime createDate;
    private String avatarUrl;
    private String firstName;
    private String lastName;
    private String description;
    private String role = "TEACHER";
}
