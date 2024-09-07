package zafus.rubikbmt.rubikbmt_website.requestEntities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.entities.Event;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestUpdateRound {
    private String id;

    private String name;

    private int nextRoundCandidate;

    private boolean isDeleted;
}