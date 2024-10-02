package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article {

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

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column(columnDefinition = "LONGTEXT")
    private String thumbnail;

    // Danh sách các bình luận của bài viết
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "article_id")
    private List<Comment> comments;

    @PreUpdate
    public void preUpdate() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = generateSlugFromTitle(this.title);
        }
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    // Helper method to create a slug from the title
    public String generateSlugFromTitle(String title) {
        if (title == null || title.isEmpty()) {
            return UUID.randomUUID().toString();  // fallback if title is missing
        }

        // Normalize and remove special characters
        String slug = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("[^\\w\\s-]", "") // Remove non-word characters
                .replaceAll("\\s+", "-") // Replace spaces with hyphens
                .toLowerCase();

        // Optionally, truncate the slug to a certain length
        if (slug.length() > 50) {
            slug = slug.substring(0, 50);
        }

        // Ensure slug uniqueness (e.g., append random UUID suffix if needed)
        return slug + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
