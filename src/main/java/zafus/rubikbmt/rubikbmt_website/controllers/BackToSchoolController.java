package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/back-to-school")
public class BackToSchoolController {
//    @Autowired
//    private CompetitionService competitionService;
//
//    @GetMapping()
//    public String index(Model model) {
//        Candidate candidate = new Candidate();
//        LocalDateTime currentTime = LocalDateTime.now();
//        LocalDateTime openTime = LocalDateTime.of(2024, 8, 22, 0, 0);
//        if (currentTime.isBefore(openTime)) {
//            return "home/coming-soon";
//        } else {
//            Competition competition = competitionService.getByName("BackToSchool");
//            candidate.setCompetition(competition);
//
//            model.addAttribute("events", competition.getEvents());
//            model.addAttribute("candidate",candidate);
//            return "backToSchool/index";
//        }
//    }
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CompetitionService competitionService;
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime openTime = LocalDateTime.of(2024, 8, 22, 0, 0);
    @GetMapping()
    public String index(Model model) {
        Candidate candidate = new Candidate();
        if (currentTime.isBefore(openTime)) {
            return "home/coming-soon";
        } else {
            Competition competition = competitionService.getByName("BackToSchool");
            candidate.setCompetition(competition);

            model.addAttribute("events", competition.getEvents());
            model.addAttribute("candidate",candidate);
            return "backToSchool/index";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        Candidate candidate = new Candidate();

        if (currentTime.isBefore(openTime)) {
            return "home/coming-soon";
        } else {
            Competition competition = competitionService.getByName("BackToSchool");
            candidate.setCompetition(competition);

            model.addAttribute("events", competition.getEvents());
            model.addAttribute("candidate",candidate);
            return "backToSchool/register";
        }
    }
    @GetMapping("/general")
    public String general(Model model) {
        if (currentTime.isBefore(openTime)) {
            return "home/coming-soon";
        } else {
            return "backToSchool/general";
        }
    }

    @GetMapping("/cost")
    public String cost(Model model) {
        if (currentTime.isBefore(openTime)) {
            return "home/coming-soon";
        } else {
            return "backToSchool/cost";
        }
    }

    @GetMapping("/schedule")
    public String schedule(Model model) {
        if (currentTime.isBefore(openTime)) {
            return "home/coming-soon";
        } else {
            return "backToSchool/schedule";
        }
    }

    @GetMapping("/ranking")
    public String ranking(Model model) {
        if (currentTime.isBefore(openTime)) {
            return "home/coming-soon";
        } else {
            return "backToSchool/ranking";
        }
    }

    @GetMapping("location")
    public String location(Model model) {
        if (currentTime.isBefore(openTime)) {
            return "home/coming-soon";
        } else {
            return "backToSchool/location";
        }
    }
}
