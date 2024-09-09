package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zafus.rubikbmt.rubikbmt_website.IO.TimeComponent;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.Collator;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    private int rankRound;

    private static final int SCALE = 2; // Làm tròn đến 2 chữ số thập phân

    public static Solve findBest(List<Solve> solves) {
        return solves.stream()
                .filter(solve -> !solve.isDNF())
                .min(Comparator.comparing(Solve::getDuration))
                .orElse(null);
    }

    public static Solve findWorst(List<Solve> solves) {
        return solves.stream()
                .max(Comparator.comparing(solve -> solve.isDNF() ? Solve.DNF_DURATION : solve.getDuration()))
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

        // Tính toán thời gian trung bình
        BigDecimal sumMillis = BigDecimal.valueOf(sum.toMillis());
        BigDecimal avgMillis = sumMillis.divide(BigDecimal.valueOf(count), SCALE, RoundingMode.HALF_UP);

        // Làm tròn đến bội số gần nhất
        long roundingFactor = 10; // Chọn bội số phù hợp để làm tròn, ví dụ: 10, 100, hoặc 1000
        BigDecimal roundingFactorBD = BigDecimal.valueOf(roundingFactor);

        // Làm tròn avgMillis đến bội số gần nhất
        BigDecimal roundedAvgMillis = avgMillis.divide(roundingFactorBD, 0, RoundingMode.HALF_UP)
                .multiply(roundingFactorBD);

        // Chuyển đổi về Duration, sử dụng giá trị tính bằng milli giây
        Duration avgDuration = Duration.ofMillis(roundedAvgMillis.longValue());

        return new Solve(avgDuration, false);
    }

    public static Solve findAo5(List<Solve> solves) {
        long dnfCount = solves.stream().filter(Solve::isDNF).count();

        if (dnfCount >= 2) {
            return new Solve(Solve.DNF_DURATION, true);
        }

        List<Solve> validSolves = solves.stream()
                .filter(solve -> !solve.isDNF())
                .sorted(Comparator.comparing(Solve::getDuration))
                .toList();

        if (validSolves.size() < 3) {
            return new Solve(Duration.ZERO, false);
        }
        long subListPrefix = 1 - dnfCount;
        Duration sum = validSolves.subList(1, validSolves.size() - Integer.parseInt(String.valueOf(subListPrefix))).stream()
                .map(Solve::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        // Số lượng giá trị để tính trung bình
        int divisor = 3;

        // Chia sum và làm tròn kết quả đến 2 chữ số thập phân
        BigDecimal sumMillis = BigDecimal.valueOf(sum.toMillis());
        BigDecimal ao5Millis = sumMillis.divide(BigDecimal.valueOf(divisor), SCALE, RoundingMode.HALF_UP);

// Làm tròn ao5Millis đến bội số gần nhất của 10 (thay vì 100)
        BigDecimal roundingFactorBD = BigDecimal.valueOf(10);
        BigDecimal roundedAo5Millis = ao5Millis.divide(roundingFactorBD, 0, RoundingMode.HALF_UP)
                .multiply(roundingFactorBD);

// Chuyển đổi lại về Duration
        Duration roundedDuration = Duration.ofMillis(roundedAo5Millis.longValue());

        return new Solve(roundedDuration, false);
    }

    public List<Solve> getSolvesOrder() {
        List<Solve> solvers = solves;
        solvers.sort(Comparator.comparing(Solve::getOrderIndex));
        return solvers;
    }

    public String getDurationString(String text) {
        switch (text) {
            case "best":
                if (best != null) {
                    if(best.isDNF()) {
                        return "DNF";
                    }
                    return TimeComponent.printDuration(best.getDurationString());
                } else {
                    return "";
                }
            case "worst":
                if (worst != null) {
                    if(worst.isDNF()) {
                        return "DNF";
                    }
                    return TimeComponent.printDuration(worst.getDurationString());
                } else {
                    return "";
                }
            case "ao5":
                if (ao5 != null) {
                    if(ao5.isDNF()) {
                        return "DNF";
                    }
                    return TimeComponent.printDuration(ao5.getDurationString());
                } else {
                    return "";
                }
            default:
                if (avg != null) {
                    if(avg.isDNF()) {
                        return "DNF";
                    }
                    return TimeComponent.printDuration(avg.getDurationString());
                } else {
                    return "";
                }
        }
    }
}
