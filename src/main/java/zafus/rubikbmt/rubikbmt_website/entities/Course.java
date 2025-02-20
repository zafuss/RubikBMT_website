package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

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
    @Nullable
    private String description;
    @Nullable
    private String requirement;
    private String target;
    @Nullable
    private int minAge;
    @Nullable
    private int maxAge;
    private int minutesPerSession;
    private int numOfSessions;
    private long totalFee;
    @ManyToOne
    private CubeSubject cubeSubject;
    @ManyToOne
    private LevelRating minLevelRating;
    @ManyToOne
    private LevelRating maxLevelRating;
}
