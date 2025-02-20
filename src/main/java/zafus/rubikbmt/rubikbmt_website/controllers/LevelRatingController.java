package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.LevelRating;
import zafus.rubikbmt.rubikbmt_website.services.LevelRatingService;

@Controller
@RequestMapping("/level-ratings")
public class LevelRatingController {
    @Autowired
    private LevelRatingService levelRatingService;

    @GetMapping
    public String listLevelRatings(Model model) {
        model.addAttribute("levelRatings", levelRatingService.getAllLevelRatings());
        return "levelratings/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("levelRating", new LevelRating());
        return "levelratings/create";
    }

    @PostMapping("/create")
    public String createLevelRating(@ModelAttribute LevelRating levelRating) {
        levelRatingService.saveLevelRating(levelRating);
        return "redirect:/level-ratings";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        LevelRating levelRating = levelRatingService.getLevelRatingById(id).orElse(null);
        if (levelRating == null) {
            return "redirect:/level-ratings";
        }
        model.addAttribute("levelRating", levelRating);
        return "levelratings/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateLevelRating(@PathVariable String id, @ModelAttribute LevelRating levelRating) {
        levelRating.setId(id);
        levelRatingService.saveLevelRating(levelRating);
        return "redirect:/level-ratings";
    }

    @GetMapping("/delete/{id}")
    public String deleteLevelRating(@PathVariable String id) {
        levelRatingService.deleteLevelRating(id);
        return "redirect:/level-ratings";
    }
}