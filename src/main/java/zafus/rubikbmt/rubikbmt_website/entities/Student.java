package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Student extends User{
    @ManyToOne
    private User parent;
}
