package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.entities.Event;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateCandidate;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CompetitionService competitionService;


    @GetMapping("/register")
    public String register(Model model) {
        return "backToSchool/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Candidate candidate, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new);
            Competition competition = competitionService.getByName("BackToSchool");
            candidate.setCompetition(competition);
            model.addAttribute("errors", errors);
            model.addAttribute("events", competition.getEvents());
            model.addAttribute("candidate", candidate);
            return "backToSchool/register";
        }
        candidate.setConfirmed(false);
        candidate.setRegistrationTime(LocalDateTime.now());
        candidate.setFullName();
        candidateService.add(candidate);
        return "redirect:/back-to-school/register?success";
    }

    @GetMapping("")
    public String index(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(defaultValue = "") String keyword,
                            @RequestParam(defaultValue = "") String searchType) {

//        Pageable pageable = PageRequest.of(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.asc("isConfirmed"),
                Sort.Order.desc("registrationTime")
        ));
        Page<Candidate> candidatePage = candidateService.searchCandidates(keyword, searchType, pageable);
        model.addAttribute("candidates", candidatePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", candidatePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "candidate/index";
    }


    @GetMapping("/detail")
    public String viewCandidateDetails(@RequestParam("id") String id, Model model) {
        Candidate candidate = candidateService.findById(id);
        model.addAttribute("candidate", candidate);
        return "candidate/detail";
    }
    @GetMapping("/edit")
    public String editCandidate(@RequestParam("id") String id, Model model) {
//      Candidate candidate = candidateService.findById(id);
        RequestUpdateCandidate candidate = RequestUpdateCandidate.fromEntity(candidateService.findById(id));
        Competition competition = competitionService.getByName("BackToSchool");
        List<Event> events =  competition.getEvents();
        boolean isConfirmed = candidate.getTimeConfirmed() != null;
        model.addAttribute("candidate", candidate);
        model.addAttribute("events", events);
        model.addAttribute("isConfirmed",isConfirmed);
        return "candidate/edit";
    }

    @PostMapping("/updateCandidate")
    public String updateCandidate(@ModelAttribute("candidate") @Valid RequestUpdateCandidate candidate, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            Competition competition = competitionService.getByName("BackToSchool");
            List<Event> events =  competition.getEvents();
            model.addAttribute("errors", errors);
            model.addAttribute("candidate", candidate);
            model.addAttribute("events", events);
            return "candidate/edit";
        }
       candidateService.updateCandidate(candidate);
        return "redirect:/candidates/detail?id=" + candidate.getId();
    }

}
