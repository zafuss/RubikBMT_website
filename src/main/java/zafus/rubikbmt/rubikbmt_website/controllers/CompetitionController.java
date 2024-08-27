package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;

@Controller
@RequestMapping("/competitions")
public class CompetitionController {
    @Autowired
    private CompetitionService competitionService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("competitions", competitionService.getAll());
        return "competition/index";
    }
}
