package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Solve {
    public static final Duration DNF_DURATION = Duration.ofHours(10);

    public Solve(Duration duration, boolean isDNF) {
        this.duration = duration;
        this.isDNF = isDNF;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Duration duration;

    private String durationString;

    private boolean isDNF = false;
}