package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.Collator;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoundDetail {
    public static Collator collator = Collator.getInstance(new Locale("vi", "VN"));

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Solve best;

    @ManyToOne
    private Solve avg;

    @ManyToOne
    private Solve ao5;

    @ManyToOne
    private Solve worst;

    @ManyToOne
    private Round round;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Solve> solves;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;


    public static Solve findBest(List<Solve> solves) {
        return solves.stream()
                .filter(solve -> !solve.isDNF())
                .min(Comparator.comparing(Solve::getDuration))
                .orElse(null);
    }

    public static Solve findWorst(List<Solve> solves) {
        return solves.stream()
                .max(Comparator.comparing(solve -> solve.isDNF() ?
                        Solve.DNF_DURATION : solve.getDuration()))
                .orElse(null);
    }

    public static Solve findAvg(List<Solve> solves) {
        long count = solves.stream()
                .filter(solve -> !solve.isDNF())
                .count();

        if (count == 0) {
            return new Solve(Duration.ZERO, false);
        }

        Duration sum = solves.stream()
                .filter(solve -> !solve.isDNF())
                .map(Solve::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        Duration avgDuration = sum.dividedBy(count);
        return new Solve(avgDuration, false);
    }

    public static Solve  findAo5(List<Solve> solves) {
        long dnfCount = solves.stream().filter(Solve::isDNF).count();

        if (dnfCount >= 2) {
            return new Solve(Solve.DNF_DURATION, true);

        }

        List<Solve> validSolves = solves.stream()
                .filter(solve -> !solve.isDNF())
                .sorted(Comparator.comparing(Solve::getDuration))
                .toList();

        if (validSolves.size() < 3) {
            return new Solve( Duration.ZERO, false);
        }

        Duration sum = validSolves.subList(1, validSolves.size() - 1).stream()
                .map(Solve::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        Duration ao5Duration = sum.dividedBy(validSolves.size() - 2);
        return new Solve(ao5Duration, false);
    }

}
