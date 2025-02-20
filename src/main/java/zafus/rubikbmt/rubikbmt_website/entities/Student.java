package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

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
    private int studiedCourse;
}
