package zafus.rubikbmt.rubikbmt_website.requestEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestUpdateArticle {

    @Id
    private String id;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne
    private User author;

    private LocalDateTime createdAt;

    private Category category;

    @Column(columnDefinition = "LONGTEXT")
    private String thumbnail;

    private List<Comment> comments;

    private boolean isDeleted;

    private boolean isHot;

    private int view;

    public static RequestUpdateArticle fromEntity(Article article) {
        RequestUpdateArticle dto = new RequestUpdateArticle();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setAuthor(article.getAuthor());
        dto.setCategory(article.getCategory());
        dto.setContent(article.getContent());
        dto.setThumbnail(article.getThumbnail());
        dto.setDeleted(article.isDeleted());
        dto.setView(article.getView());
        dto.setComments(article.getComments());
        dto.setDescription(article.getDescription());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setHot(article.isHot());

        return dto;
    }
}
