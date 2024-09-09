package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final CompetitionService competitionService;
    private final CandidateService candidateService;

    public GlobalControllerAdvice(CompetitionService competitionService, CandidateService candidateService) {
        this.competitionService = competitionService;
        this.candidateService = candidateService;
    }

    @ModelAttribute
    public void addCompetitionsToModel(Model model) {
        List<Competition> competitions = competitionService.getAll();
        model.addAttribute("competitions", competitions);
        List<Object[]> statistics = candidateService.getCountCandidatesByEvent();
        model.addAttribute("statistics", statistics);
    }

//    @GetMapping()
//    public void getCandidateStatistics(Model model) {
//        List<Object[]> statistics = candidateService.getCountCandidatesByEvent();
//        model.addAttribute("statistics", statistics);
//    }
}