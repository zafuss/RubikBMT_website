package zafus.rubikbmt.rubikbmt_website.DTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zafus.rubikbmt.rubikbmt_website.IO.TimeComponent;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Round;
import zafus.rubikbmt.rubikbmt_website.entities.Solve;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoundDetailDTO {

    private String best;

    private String avg;

    private String ao5;

    private String worst;

//    private Round round;

    private List<SolvesDTO> solves;

    private String fullName;

    private int rankRound;

    private String firstName;

    private int nextRoundCandidate;

    public String getTimeDurationString(String durationString){
        return TimeComponent.printDuration(durationString);
    }
}
