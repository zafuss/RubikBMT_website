package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private int minSpeed;
    private int maxSpeed;
    @ManyToOne
    @JoinColumn(name = "rating_id", nullable = true)
    private LevelRating levelRating;

    @ManyToOne
    private CubeSubject cubeSubject;
}
