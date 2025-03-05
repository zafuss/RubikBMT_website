package zafus.rubikbmt.rubikbmt_website.requestEntities;

import jakarta.persistence.*;
import lombok.*;
import zafus.rubikbmt.rubikbmt_website.entities.Course;
import zafus.rubikbmt.rubikbmt_website.entities.Teacher;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateClassroom {
    private String id;
    private String name;
    private long actualFee;
    private String description;
    private Teacher teacher;
}
