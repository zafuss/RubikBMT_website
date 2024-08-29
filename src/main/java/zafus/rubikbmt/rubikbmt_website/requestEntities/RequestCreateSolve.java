package zafus.rubikbmt.rubikbmt_website.requestEntities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestCreateSolve {
    private String roundId;
    private String candidateId;

    private String solve1;
    private String solve2;
    private String solve3;
    private String solve4;
    private String solve5;

    public String getSolve(int index) {
        switch (index) {
            case 1:
                return solve1;
            case 2:
                return solve2;
            case 3:
                return solve3;
            case 4:
                return solve4;
            default:
                return solve5;
        }
    }
}
