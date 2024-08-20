package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidStudentEmail;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidStudentPhoneNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Size(min = 1, max = 50, message = "Họ và tên đệm phải có từ 1 đến 50 ký tự")
    private String lastName;
    @Size(min = 1, max = 50, message = " Tên phải có từ 1 đến 50 ký tự")
    private String firstName;
//    @Size(min = 1, max = 50, message = "Họ và tên phụ huynh phải có từ 1 đến 50 ký tự")
    private String parentName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày tháng không được bỏ trống.")
    private LocalDate dateOfBirth;
    @Length(min = 10, max = 10, message = "Phone phải có 10 số")
    @Pattern(regexp = "^[0-9]*$", message = "Phone phải là số")
    @ValidStudentPhoneNumber
    private String phoneNumber;
    @Size(min = 1, max = 50, message = "Email phải có từ 1 đến 50 ký tự")
    @Email(message = "Không đúng định dạng Email")
    @ValidStudentEmail
    private String email;
    private String imageUrl;
    @ManyToOne
    private LearningType learningType;
    private boolean isConfirmed;
    @ManyToOne
    private Mentor mentor;

    private String fullName;
    public void setFullName() {
        this.fullName = this.firstName + " " + this.lastName;
    }
}


