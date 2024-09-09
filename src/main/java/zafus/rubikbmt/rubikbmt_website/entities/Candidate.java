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
import org.springframework.lang.Nullable;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidCandidateEmail;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidCandidatePhoneNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(min = 1, max = 50, message = "Họ và tên đệm phải có từ 1 đến 50 ký tự")
    private String lastName;

    @Size(min = 1, max = 50, message = "Tên phải có từ 1 đến 50 ký tự")
    private String firstName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày tháng không được bỏ trống.")
    private LocalDate dateOfBirth;

    @Length(min = 10, max = 10, message = "Số điện thoại phải có 10 số")
    @Pattern(regexp = "^[0-9]*$", message = "Số điện thoại phải là số")
    @ValidCandidatePhoneNumber
    private String phoneNumber;

    @Size(min = 1, max = 50, message = "Email phải có từ 1 đến 50 ký tự")
    @Email(message = "Không đúng định dạng Email")
    @ValidCandidateEmail
    private String email;

    private String fullName;

    @ManyToOne
    private Competition competition;

    private String note;
    private LocalDateTime registrationTime;
    private boolean isConfirmed;
    private LocalDateTime timeConfirmed;

    @ManyToMany
    private List<Event> events;

    @Nullable
    @Column(unique = true) // Đảm bảo tính duy nhất cho checkinID
    private Integer checkinID;

    public void setFullName() {
        this.fullName = this.lastName + " " + this.firstName;
    }

    // Hàm tự động tạo checkinID nếu nó không tồn tại
//    @PrePersist
//    @PreUpdate
//    public void generateCheckinID() {
//        if (this.checkinID == null) {
//            // Tìm giá trị lớn nhất hiện có của checkinID trong cơ sở dữ liệu
//            Integer maxCheckinID = /* logic để lấy giá trị max từ cơ sở dữ liệu */;
//            this.checkinID = (maxCheckinID != null ? maxCheckinID + 1 : 1);
//        }
//    }
}