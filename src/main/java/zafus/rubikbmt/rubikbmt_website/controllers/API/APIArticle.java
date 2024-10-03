package zafus.rubikbmt.rubikbmt_website.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.DTO.CommentDTO;
import zafus.rubikbmt.rubikbmt_website.entities.Article;
import zafus.rubikbmt.rubikbmt_website.entities.Comment;
import zafus.rubikbmt.rubikbmt_website.entities.Category;
import zafus.rubikbmt.rubikbmt_website.services.ArticleService;
import zafus.rubikbmt.rubikbmt_website.services.CategoryService;
import zafus.rubikbmt.rubikbmt_website.services.CommentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class APIArticle {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/{id}/addComment")
    public ResponseEntity<CommentDTO> addComment(@PathVariable String id,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("content") String content) {
        // Lấy bài viết bằng id
        Article article = articleService.getArticleById(id);

        // Tạo đối tượng Comment mới
        Comment comment = new Comment();
        comment.setName(name);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now()); // Cập nhật thời gian bình luận

        // Lưu comment vào bài viết
        Comment newComment = commentService.save(comment, article);

        // Tạo đối tượng CommentDTO để trả về dưới dạng JSON
        CommentDTO newDtoComment = new CommentDTO();
        newDtoComment.setName(newComment.getName());
        newDtoComment.setContent(newComment.getContent());
        newDtoComment.setId(newComment.getId());
        // Định dạng thời gian cho giao diện
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        newDtoComment.setCreatedAt(newComment.getCreatedAt().format(formatter));

        // Trả về đối tượng JSON chứa thông tin bình luận mới
        return ResponseEntity.ok(newDtoComment);
    }

    @PostMapping("/{id}/addReply")
    public ResponseEntity<CommentDTO> addReply(@PathVariable String id,
                                               @RequestParam("parentCommentId") String parentCommentId,
                                               @RequestParam("name") String name,
                                               @RequestParam("content") String content) {
        Article article = articleService.getArticleById(id);
        Comment parentComment = commentService.findById(parentCommentId);

        Comment reply = new Comment();
        reply.setName(name);
        reply.setContent(content);
        reply.setParentComment(parentComment);
        reply.setCreatedAt(LocalDateTime.now());
        Comment newReply = commentService.save(reply, article);

        // Tạo đối tượng CommentDTO để trả về dưới dạng JSON
        CommentDTO newDtoReply = new CommentDTO();
        newDtoReply.setName(newReply.getName());
        newDtoReply.setContent(newReply.getContent());
        newDtoReply.setCreatedAt(newReply.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        newDtoReply.setId(newReply.getId());
        // Trả về JSON chứa thông tin reply mới
        return ResponseEntity.ok(newDtoReply);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        // Assume you have a CategoryService that fetches categories from your database
        return categoryService.findAll();
    }
}