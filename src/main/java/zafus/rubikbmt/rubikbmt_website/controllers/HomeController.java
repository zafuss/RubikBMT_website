package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import zafus.rubikbmt.rubikbmt_website.services.ArticleService;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;
import zafus.rubikbmt.rubikbmt_website.services.LearningTypeService;
import zafus.rubikbmt.rubikbmt_website.services.MentorService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping()
public class HomeController {
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private LearningTypeService learningTypeService;
    @Autowired
    private MentorService mentorService;
    @Autowired
    private ArticleService articleService;
    @GetMapping("/cube")
    public String cube(){
        return "home/cube";
    }
    @GetMapping()
    public String index(HttpServletRequest request, Model model) {
//        Boolean isBackToSchool = (Boolean) request.getAttribute("isBackToSchool");
//        if (Boolean.TRUE.equals(isBackToSchool)) {
//            Candidate candidate = new Candidate();
//            LocalDateTime currentTime = LocalDateTime.now();
//            LocalDateTime openTime = LocalDateTime.of(2024, 8, 22, 0, 0);
//            if (currentTime.isBefore(openTime)) {
//                return "home/coming-soon";
//            } else {
//                Competition competition = competitionService.getByName("BackToSchool");
//                candidate.setCompetition(competition);
//
//                model.addAttribute("events", competition.getEvents());
//                model.addAttribute("candidate",candidate);
//                return "backToSchool/index"; // Trả về view cho subdomain
//            }
//        } else {
//            List<LearningType> learningTypes = learningTypeService.findAll();
//            List<Mentor> mentors = mentorService.findAll();
//            model.addAttribute("learningTypes", learningTypes);
//            model.addAttribute("mentors", mentors);
//            model.addAttribute("student", new Student());
//            return "home/index"; // Trả về view cho domain chính
//        }
        List<LearningType> learningTypes = learningTypeService.findAll();
        List<Mentor> mentors = mentorService.findAll();
        model.addAttribute("learningTypes", learningTypes);
        model.addAttribute("mentors", mentors);
        model.addAttribute("student", new Student());
        model.addAttribute("latestArticles", articleService.findTop6LatestArticles(""));
        return "home/index";
    }

}
