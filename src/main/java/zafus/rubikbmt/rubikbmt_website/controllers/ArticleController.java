package zafus.rubikbmt.rubikbmt_website.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import zafus.rubikbmt.rubikbmt_website.DTO.imgur.ImgurResponse;
import zafus.rubikbmt.rubikbmt_website.entities.Article;
import zafus.rubikbmt.rubikbmt_website.entities.Category;
import zafus.rubikbmt.rubikbmt_website.entities.Comment;
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateArticle;
import zafus.rubikbmt.rubikbmt_website.services.*;
import zafus.rubikbmt.rubikbmt_website.utilities.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/articles")

public class ArticleController {
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CommentService commentService;
    private final ImgurService imgurService;

    public User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            User user = userService.findByUserName(((UserDetails) principal).getUsername());
            return user;
        }
        return null;
    }

    @GetMapping
    public String listArticles(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("articles", articleService.findArticlesSortedByIsDeletedAndCreatedAtDesc());

        return "article/index";
    }

    @GetMapping("/search")
    public String listArticles(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "") String keyword,
                               @RequestParam(defaultValue = "") String searchType) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Article> articlesPage = articleService.searchArticles(keyword, searchType, pageable);


        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("articles", articlesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlesPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "article/index";
    }

    @GetMapping("/list")
    public String listArticlesByCategories(Model model) {
        List<Category> categories = categoryService.findAll().stream().filter((c) -> !c.isDeleted()).toList(); // Lấy danh sách các danh mục

        // Tạo map để chứa danh sách bài viết theo danh mục
        Map<Category, List<Article>> articlesByCategory = new HashMap<>();
        for (Category category : categories) {
            List<Article> articles = articleService.findTop4ByCategory(category);
            articlesByCategory.put(category, articles);
        }
        model.addAttribute("latestArticles", articleService.findTop6LatestArticles(""));
        model.addAttribute("articlesByCategory", articlesByCategory);
        return "article/list";
    }

    @GetMapping("/latest")
    public String latestArticles(Model model) {
        model.addAttribute("latestArticles", articleService.findLatestArticle().stream().filter((a) -> !a.isDeleted()).collect(Collectors.toList()));
        return "article/latest";
    }

    @GetMapping("/category/{categoryId}")
    public String getArticlesByCategory(@PathVariable String categoryId, Model model) {

        if (categoryService.findById(categoryId).isDeleted()) {
            return "forward:/error/404.html";
        }
        model.addAttribute("articles", articleService.getArticlesByCategory(categoryId));
        model.addAttribute("currentCategory", categoryService.findById(categoryId));
        model.addAttribute("categories", categoryService.findDifferentCategories(categoryId));

        return "article/byCategory";
    }

    @GetMapping("/detail/{id}")
    public String viewArticle(@PathVariable String id, Model model) {
        Article article = articleService.getArticleById(id);
        if (article.isDeleted()) {
            return "forward:/error/404.html";
        }
        model.addAttribute("articles", articleService.findTop5ByCategoryAndOrderByCreatedAtDesc(id));
        model.addAttribute("latestArticles", articleService.findTop6LatestArticles(id));

        article.setView(article.getView() + 1);

        articleService.updateArticle(article);
        // Sắp xếp bình luận theo createdAt
        List<Comment> sortedComments = article.getComments().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed()) // Sắp xếp theo thời gian tăng dần
                .collect(Collectors.toList());

        // Sắp xếp replies cho từng bình luận
        for (Comment comment : sortedComments) {
            List<Comment> sortedReplies = comment.getReplies().stream()
                    .sorted(Comparator.comparing(Comment::getCreatedAt).reversed()) // Sắp xếp phản hồi theo thời gian
                    .collect(Collectors.toList());
            comment.setReplies(sortedReplies); // Cập nhật lại danh sách replies đã sắp xếp
        }

        // Cập nhật lại danh sách comments đã sắp xếp vào article
        article.setComments(sortedComments);
        model.addAttribute("article", article);

        return "article/detail";
    }

    @GetMapping("/add")
    public String createArticleForm(Model model) {
        Article article = new Article();
        article.setAuthor(getLoggedInUser());
        model.addAttribute("article", article);
        model.addAttribute("categories", categoryService.findAll());
        return "article/add";
    }

    @PostMapping("/add")
    public String saveArticle(
            @ModelAttribute Article article,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile) throws IOException {

        if (!thumbnailFile.isEmpty()) {
            // Gọi hàm lưu file và trả về URL
//            String thumbnailUrl = FileUtils.saveFile(thumbnailFile);
            ImgurResponse response = imgurService.uploadImage(thumbnailFile);
            String imageUrl = response.getData().getLink();
            article.setThumbnail(imageUrl);
        }

        // Lưu bài viết vào cơ sở dữ liệu
        articleService.saveArticle(article, article.getCategory().getId(), article.getAuthor().getId());

        return "redirect:/articles";
    }

    private String saveThumbnailFile(MultipartFile file) {
        // Ví dụ: lưu file vào thư mục "images" và trả về URL
        String filename = file.getOriginalFilename();
        Path filepath = Paths.get("src/main/resources/static/images", filename);

        try {
            Files.write(filepath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/images/" + filename;
    }

    @PostMapping("/{id}/addComment")
    public String addComment(@PathVariable String id,
                             @RequestParam("name") String name,
                             @RequestParam("content") String content) {
        Article article = articleService.getArticleById(id);
        Comment comment = new Comment();
        comment.setName(name);
        comment.setContent(content);
        commentService.save(comment, article);
        return "redirect:/articles/detail/" + id;
    }

    @PostMapping("/{id}/addReply")
    public String addReply(@PathVariable String id,
                           @RequestParam("parentCommentId") String parentCommentId,
                           @RequestParam("name") String name,
                           @RequestParam("content") String content) {
        Article article = articleService.getArticleById(id);
        Comment parentComment = commentService.findById(parentCommentId);

        Comment reply = new Comment();
        reply.setName(name);
        reply.setContent(content);
        reply.setParentComment(parentComment);

        commentService.save(reply, article);
        return "redirect:/articles/detail/" + id;
    }

    // Phương thức để hiển thị trang chỉnh sửa bài viết
    @GetMapping("/edit/{id}")
    public String editArticle(@PathVariable String id, Model model) {
        RequestUpdateArticle article = RequestUpdateArticle.fromEntity(articleService.getArticleById(id));

        boolean isDeleted = article.isDeleted();
        boolean isHot = article.isHot();
        LocalDateTime currentCreatedAt = article.getCreatedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        model.addAttribute("article", article);
        model.addAttribute("deleted", isDeleted);
        model.addAttribute("hotArticle", isHot);
        model.addAttribute("currentCreatedAt",  currentCreatedAt.format(formatter));
        model.addAttribute("categories", categoryService.findAll()); // Giả sử bạn có phương thức này để lấy danh sách các danh mục

        return "article/edit"; // Trả về trang chỉnh sửa bài viết
    }

    // Phương thức để xử lý cập nhật bài viết
    @PostMapping("/edit/{id}")
    public String updateArticle(@PathVariable String id,
                                @RequestParam String title,
                                @RequestParam String description,
                                @RequestParam String content,
                                @RequestParam boolean deleted,
                                @RequestParam boolean hot,
                                @RequestParam LocalDateTime currentCreatedAt,
                                @RequestParam(required = false) String thumbnail,
                                @RequestParam(required = false) MultipartFile thumbnailFile,
                                @RequestParam String categoryId) throws IOException {

        Article article = articleService.getArticleById(id);
        if (!thumbnailFile.isEmpty()) {
            // Gọi hàm lưu file và trả về URL
            ImgurResponse response = imgurService.uploadImage(thumbnailFile);
            String imageUrl = response.getData().getLink();
            article.setThumbnail(imageUrl);
        } else {
            article.setThumbnail(thumbnail);
        }
        article.setTitle(title);
        article.setDescription(description);
        article.setContent(content);
        article.setCategory(categoryService.findById(categoryId));
        article.setDeleted(deleted);
        article.setHot(hot);
        article.setCreatedAt(currentCreatedAt);
        articleService.updateArticle(article);
        return "redirect:/articles"; // Chuyển hướng đến trang chi tiết bài viết đã cập nhật
    }
}