package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
//@DiscriminatorValue("STUDENT")
//@Entity
@Getter
@Setter
@Entity
public class Student extends User{
//    @ManyToOne
//    @Nullable
//    private User parent;
    public Student(User user, int studiedCourse, String parentName, String parentPhoneNumber) {
        super(user.getUsername(), user.getEmail(), user.getPassword(), user.getFirstName(),
                user.getLastName(), user.getRoles(), user.getPhoneNumber());
        this.setId(user.getId())    ;
        this.studiedCourse = studiedCourse;
        this.parentName = parentName;
        this.parentPhoneNumber = parentPhoneNumber;

    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private int studiedCourse;
    @Nullable
    private String parentName;
    @Nullable
    private String parentPhoneNumber;
}
