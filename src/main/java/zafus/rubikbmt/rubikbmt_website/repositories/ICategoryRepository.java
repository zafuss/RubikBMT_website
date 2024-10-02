package zafus.rubikbmt.rubikbmt_website.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.Article;
import zafus.rubikbmt.rubikbmt_website.entities.Category;

import java.util.List;

public interface ICategoryRepository extends JpaRepository<Category, String> {
    @Query("SELECT c FROM Category c WHERE c.id != :categoryId ORDER BY c.name DESC")
    List<Category> findDifferentCategories(@Param("categoryId") String categoryId);
}