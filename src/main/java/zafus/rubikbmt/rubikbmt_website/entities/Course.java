package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotNull
    private String name;
    private String description;
    private String requirement;
    private String target;
    private int minAge;
    private int maxAge;
    private String minutesPerSession;
    private int numOfSessions;
    private long totalFee;
    @ManyToOne
    private CubeType cubeType;
    @ManyToOne
    private Level level;
}
