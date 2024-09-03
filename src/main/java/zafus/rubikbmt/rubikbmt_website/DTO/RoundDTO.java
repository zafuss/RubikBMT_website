package zafus.rubikbmt.rubikbmt_website.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoundDTO {
    private String competitionId;
    private String eventId;
    private String roundDetailId;
    private String name;
}
