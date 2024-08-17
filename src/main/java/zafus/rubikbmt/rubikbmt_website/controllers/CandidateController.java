package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;
    @PostMapping("/register")
    public String register(@ModelAttribute Candidate candidate) {
        candidate.setConfirmed(false);
        candidate.setRegistrationTime(LocalDateTime.now());
        candidateService.add(candidate);
        return "redirect:/?success";
    }

}
