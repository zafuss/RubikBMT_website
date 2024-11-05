package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.Category;
import zafus.rubikbmt.rubikbmt_website.services.CategoryService;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String index(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "category/index"; // Tạo file category/index.html
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("category", new Category());
        return "category/add"; // Tạo file category/add.html
    }

    @PostMapping
    public String save(@ModelAttribute Category category) {
        category.setDeleted(false); // Mặc định khi tạo mới
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable String id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "category/detail"; // Tạo file category/detail.html
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable String id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "category/edit"; // Trả về tên view
    }

    @PostMapping("/edit")
    public String updateCategory(@ModelAttribute Category category) {
        categoryService.save(category); // Lưu category đã chỉnh sửa
        return "redirect:/categories"; // Chuyển hướng về danh sách category
    }
}