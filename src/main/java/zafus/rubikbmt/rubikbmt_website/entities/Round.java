package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@Entity
@AllArgsConstructor
@Getter
@Setter
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private int numOfCandidate;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @Nullable
    private int nextRoundCandidate = 0;

    private boolean isDeleted = false;

    private LocalDateTime createTime;

}