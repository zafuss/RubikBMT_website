package zafus.rubikbmt.rubikbmt_website.requestEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zafus.rubikbmt.rubikbmt_website.entities.Classroom;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RequestUpdateSession {
    private String id;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Classroom classroom;
    private String callbackUrl;
}
