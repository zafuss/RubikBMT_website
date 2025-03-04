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
    @Query("SELECT a FROM Article a WHERE a.category.id = :categoryId AND a.isDeleted = false ORDER BY a.createdAt DESC")
    List<Article> findByCategoryId(String categoryId);

    @Query("SELECT a FROM Article a WHERE a.category = (SELECT ar.category FROM Article ar WHERE ar.id = :articleId) " +
            "AND a.id != :articleId AND a.isDeleted = false ORDER BY a.createdAt DESC")
    List<Article> findTop5ByCategoryAndOrderByCreatedAtDesc(@Param("articleId") String articleId);

    @Query("SELECT a FROM Article a WHERE a.id != :excludedId AND a.isDeleted = false ORDER BY a.isHot DESC, a.createdAt DESC ")
    List<Article> findTop6LatestArticles(@Param("excludedId") String excludedId, Pageable pageable);

    @Query("SELECT a FROM Article a ORDER BY a.createdAt DESC ")
    List<Article> findLatestArticles();

    List<Article> findArticlesByCategory(Category category);

    @Query("SELECT a FROM Article a WHERE a.category = :category AND a.isDeleted = false ORDER BY a.createdAt DESC")
    List<Article> findTop4ByCategory(@Param("category") Category category);

    @Query("SELECT a FROM Article a WHERE a.category = :category ORDER BY a.createdAt DESC")
    List<Article> findDifferentCategories(@Param("category") String categoryId);

    Page<Article> findByTitleContaining(String title, Pageable pageable);

    @Query("SELECT a from Article a where a.author.userName like %:username%")
    Page<Article> findByAuthor(String username, Pageable pageable);;

    @Query("SELECT a from Article a where a.category.id = :category")
    Page<Article> findByCategory(String category, Pageable pageable);

    @Query("SELECT c FROM Article c WHERE " +
            "STR(c.createdAt) LIKE %:input%")
    List<Article> findByCreateAt(@Param("input") String input);

    List<Article> findByAuthorContaining(User author, Pageable pageable);

    @Query("SELECT a FROM Article a ORDER BY CASE WHEN a.isDeleted = true THEN 1 ELSE 0 END, a.createdAt DESC")
    List<Article> findArticlesSortedByIsDeletedAndCreatedAtDesc();
}