package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.Level;
import zafus.rubikbmt.rubikbmt_website.services.CubeSubjectService;
import zafus.rubikbmt.rubikbmt_website.services.LevelRatingService;
import zafus.rubikbmt.rubikbmt_website.services.LevelService;

import java.util.List;

@Controller
@RequestMapping("/levels")
public class LevelController {
    @Autowired
    private LevelService levelService;

    @Autowired
    private LevelRatingService levelRatingService;

    @Autowired
    private CubeSubjectService cubeSubjectService;
    @GetMapping
    public String listLevels(Model model) {
        List<Level> levels = levelService.getAllLevels();
        model.addAttribute("levels", levels);
        return "levels/index"; // Trả về trang Thymeleaf: src/main/resources/templates/levels/index.html
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("level", new Level());
        model.addAttribute("ratings", levelRatingService.getAllLevelRatings()); // Thêm danh sách LevelRating
        model.addAttribute("cubes", cubeSubjectService.getAllCubeSubjects());
        return "levels/create";
    }
    @PostMapping("/create")
    public String createLevel(@ModelAttribute Level level) {
        levelService.createLevel(level);
        return "redirect:/levels";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Level level = levelService.getLevelById(id).orElse(null);
        if (level == null) {
            return "redirect:/levels";
        }
        model.addAttribute("level", level);
        model.addAttribute("cubes", cubeSubjectService.getAllCubeSubjects());
        model.addAttribute("ratings", levelRatingService.getAllLevelRatings()); // Thêm danh sách LevelRating
        return "levels/edit";
    }
    @PostMapping("/edit/{id}")
    public String updateLevel(@PathVariable String id, @ModelAttribute Level level) {
        levelService.updateLevel(id, level);
        return "redirect:/levels";
    }

    @GetMapping("/delete/{id}")
    public String deleteLevel(@PathVariable String id) {
        levelService.deleteLevel(id);
        return "redirect:/levels";
    }
}