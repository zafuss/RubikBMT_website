package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;

@Controller
@RequestMapping("/")

public class HomeController {
    @Autowired
    private CompetitionService competitionService;

    @GetMapping("/cube")
    public String cube(){
        return "home/cube";
    }
    @GetMapping
    public String index(HttpServletRequest request, Model model) {
        Boolean isBackToSchool = (Boolean) request.getAttribute("isBackToSchool");
        if (Boolean.TRUE.equals(isBackToSchool)) {
            Candidate candidate = new Candidate();
            Competition competition = competitionService.getByName("BackToSchool");
            candidate.setCompetition(competition);

            model.addAttribute("events", competition.getEvents());
            model.addAttribute("candidate",candidate);
            return "backToSchool/index"; // Trả về view cho subdomain
        } else {
            return "home/index"; // Trả về view cho domain chính
        }
    }

}
