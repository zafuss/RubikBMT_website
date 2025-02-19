package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
//@DiscriminatorValue("STUDENT")
//@Entity
public class Student extends User{
    @ManyToOne
    @Nullable
    private User parent;
    private int studiedCourse;
}
