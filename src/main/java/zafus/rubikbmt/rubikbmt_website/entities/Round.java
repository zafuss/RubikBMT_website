package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
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
}