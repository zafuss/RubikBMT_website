package zafus.rubikbmt.rubikbmt_website.DTO;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CommentDTO {
    private String id;
    private String name;
    private String content;
    private String createdAt;
}
