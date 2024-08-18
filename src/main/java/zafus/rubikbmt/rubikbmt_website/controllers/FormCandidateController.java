package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateCandidate;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;
import zafus.rubikbmt.rubikbmt_website.entities.Event;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/form")
public class FormCandidateController {

    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CompetitionService competitionService;
    @GetMapping("")
    public String formManage(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Candidate> candidatePage = candidateService.searchCandidates(keyword, pageable);
        model.addAttribute("candidates", candidatePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", candidatePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "formCandidate/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model) {
        return formManage(model, page, size, keyword);
    }


    @GetMapping("/edit")
    public String editCandidate(@RequestParam("id") String id, Model model) {
        Candidate candidate = candidateService.findById(id);
        Competition competition = competitionService.getByName("BackToSchool");
        List<Event> events =  competition.getEvents();
        model.addAttribute("candidate", candidate);
        model.addAttribute("events", events);
        return "formCandidate/editCandidate";
    }

    @GetMapping("/details")
    public String viewCandidateDetails(@RequestParam("id") String id, Model model) {
        Candidate candidate = candidateService.findById(id);
        model.addAttribute("candidate", candidate);
        return "formCandidate/candidateDetails";
    }
    @PostMapping("/updateCandidate")
    public String updateCandidate(@ModelAttribute RequestUpdateCandidate candidate) {
        candidateService.updateCandidate(candidate);
        return "redirect:/form/details?id=" + candidate.getId();
    }
}
