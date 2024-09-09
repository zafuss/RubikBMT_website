package zafus.rubikbmt.rubikbmt_website.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zafus.rubikbmt.rubikbmt_website.IO.TimeComponent;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolvesDTO {

    private String durationString;

    private boolean isDNF = false;

    private int orderIndex = 0;

    public String getTimeDurationString(){
        return TimeComponent.printDuration(durationString);
    }
}
