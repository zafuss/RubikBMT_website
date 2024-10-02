package zafus.rubikbmt.rubikbmt_website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zafus.rubikbmt.rubikbmt_website.entities.Category;
import zafus.rubikbmt.rubikbmt_website.repositories.ICategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    public List<Category> findDifferentCategories(String categoryId) {
        return categoryRepository.findDifferentCategories(categoryId);
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public Category findById(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteById(String id) {
        categoryRepository.deleteById(id);
    }
}