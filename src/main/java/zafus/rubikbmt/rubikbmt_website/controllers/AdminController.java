package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zafus.rubikbmt.rubikbmt_website.components.SessionListener;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;

@Controller
@RequestMapping("")

public class AdminController {
    @Autowired
    private SessionListener sessionListener;

    @Autowired
    private CompetitionService competitionService;

    @Autowired
    private CandidateService candidateService;

    @GetMapping("/dashboard")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("currentVisitors", sessionListener.getActiveSessions());
        model.addAttribute("dailyVisitors", sessionListener.getDailyVisitors());
        return "admin/index";
    }


}
