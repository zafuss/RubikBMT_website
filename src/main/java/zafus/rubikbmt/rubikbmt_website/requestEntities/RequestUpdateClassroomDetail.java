package zafus.rubikbmt.rubikbmt_website.requestEntities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zafus.rubikbmt.rubikbmt_website.entities.Classroom;
import zafus.rubikbmt.rubikbmt_website.entities.Student;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateClassroomDetail {
    private String id;
    private Student student;
    private Classroom classroom;
    private long paid;
}
