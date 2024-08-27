package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final CompetitionService competitionService;

    public GlobalControllerAdvice(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @ModelAttribute
    public void addCompetitionsToModel(Model model) {
        List<Competition> competitions = competitionService.getAll();
        model.addAttribute("competitions", competitions);
    }
}