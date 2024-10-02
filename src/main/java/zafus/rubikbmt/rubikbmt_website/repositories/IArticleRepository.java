package zafus.rubikbmt.rubikbmt_website.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.Article;
import zafus.rubikbmt.rubikbmt_website.entities.Category;
import zafus.rubikbmt.rubikbmt_website.entities.User;

import java.util.List;

public interface IArticleRepository extends JpaRepository<Article, String> {
    @Query("SELECT a FROM Article a WHERE a.category.id = :categoryId ORDER BY a.createdAt DESC")
    List<Article> findByCategoryId(String categoryId);

    @Query("SELECT a FROM Article a WHERE a.category = (SELECT ar.category FROM Article ar WHERE ar.id = :articleId) " +
            "AND a.id != :articleId ORDER BY a.createdAt DESC")
    List<Article> findTop5ByCategoryAndOrderByCreatedAtDesc(@Param("articleId") String articleId);

    @Query("SELECT a FROM Article a WHERE a.id != :excludedId ORDER BY a.createdAt DESC")
    List<Article> findTop6LatestArticles(@Param("excludedId") String excludedId, Pageable pageable);

    @Query("SELECT a FROM Article a ORDER BY a.createdAt DESC")
    List<Article> findLatestArticles();

    List<Article> findArticlesByCategory(Category category);

    @Query("SELECT a FROM Article a WHERE a.category = :category ORDER BY a.createdAt DESC")
    List<Article> findTop4ByCategory(@Param("category") Category category);

    @Query("SELECT a FROM Article a WHERE a.category = :category ORDER BY a.createdAt DESC")
    List<Article> findDifferentCategories(@Param("category") String categoryId);

    Page<Article> findByTitleContaining(String title, Pageable pageable);

    @Query("SELECT c FROM Article c WHERE " +
            "STR(c.createdAt) LIKE %:input%")
    List<Article> findByCreateAt(@Param("input") String input);

    List<Article> findByAuthorContaining(User author, Pageable pageable);
}