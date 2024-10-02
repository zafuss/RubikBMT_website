package zafus.rubikbmt.rubikbmt_website.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import zafus.rubikbmt.rubikbmt_website.entities.Article;
import zafus.rubikbmt.rubikbmt_website.entities.Category;
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.repositories.IArticleRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.ICategoryRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.IUserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {
    @Autowired
    private IArticleRepository articleRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IUserRepository userRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> findTop5ByCategoryAndOrderByCreatedAtDesc(String articleId) {
        return articleRepository.findTop5ByCategoryAndOrderByCreatedAtDesc(articleId);
    }

    public List<Article> findTop6LatestArticles(String articleId) {
        Pageable pageable = PageRequest.of(0, 6);
        return articleRepository.findTop6LatestArticles(articleId, pageable);
    }

//    public Page<Article> searchArticles(String keyword, String searchType, Pageable pageable) {
//        switch (searchType) {
//            case "title":
//                return articleRepository.findByTitleContaining(keyword, pageable);
//            case "date":
//                return articleRepository.findByCreateAt(date)
//        }
//    }

    public Article getArticleById(String id) {
        return articleRepository.findById(id).orElse(null);
    }

    public List<Article> findTop4ByCategory(Category category) {
        return articleRepository.findTop4ByCategory(category);
    }
    public List<Article> findLatestArticle() {
        return articleRepository.findLatestArticles();
    }

    public void saveArticle(Article article, String categoryId, String userId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        article.setCategory(category);
        article.setAuthor(user);
        article.setId(article.generateSlugFromTitle(article.getTitle()));
        articleRepository.save(article);
    }

    public void updateArticle(Article article) {
        articleRepository.save(article);
    }

    public List<Article> getArticlesByCategory(String categoryId) {
        return articleRepository.findByCategoryId(categoryId);
    }
}