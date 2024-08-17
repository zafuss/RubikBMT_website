package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CompetitionService competitionService;


    @GetMapping("/register")
    public String register(Model model) {
        return "/backToSchool/index";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Candidate candidate, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new);
            Competition competition = competitionService.getByName("BackToSchool");
            candidate.setCompetition(competition);
            model.addAttribute("errors", errors);
            model.addAttribute("events", competition.getEvents());
            model.addAttribute("candidate",candidate);
            return "/backToSchool/index";
        }
        candidate.setConfirmed(false);
        candidate.setRegistrationTime(LocalDateTime.now());
        candidateService.add(candidate);
        return "redirect:/?success";
    }

}
