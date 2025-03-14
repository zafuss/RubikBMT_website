package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.servlet.http.HttpSession;
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
import zafus.rubikbmt.rubikbmt_website.services.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @Autowired
    private CompetitionService competitionService;

    @Autowired
    private EventService eventService;

    private boolean isConfirmed;
    private boolean isCheckedin;
    private List<Event> events;
    private Competition competitionTMP;
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
            model.addAttribute("candidate",candidate);
            return "backToSchool/register";
        }
        candidate.setConfirmed(false);
        candidate.setRegistrationTime(LocalDateTime.now());
        candidate.setFullName();
        candidateService.add(candidate);
        return "redirect:/back-to-school/register?success";
    }

    @GetMapping("/add")
    public String add(Model model) {
        Candidate candidate = new Candidate();

        Competition competition = competitionService.getByName("BackToSchool");
        candidate.setCompetition(competition);

        model.addAttribute("events", competition.getEvents());
        model.addAttribute("candidate", candidate);
        return "candidate/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Candidate candidate, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new);
            Competition competition = competitionService.getByName("BackToSchool");
            candidate.setCompetition(competition);
            model.addAttribute("errors", errors);
            model.addAttribute("events", competition.getEvents());
            model.addAttribute("candidate",candidate);
            return "candidate/add";
        }
        candidate.setConfirmed(true);
        candidate.setTimeConfirmed(LocalDateTime.now());
        candidate.setRegistrationTime(LocalDateTime.now());
        candidate.setCheckinID(candidateService.generateCheckinID());
        candidate.setFullName();
        Candidate newCandidate = candidateService.add(candidate);
        model.addAttribute("candidate", newCandidate);
        return "candidate/detail";
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
        events =  competition.getEvents();
        isConfirmed = candidate.getTimeConfirmed() != null;
        isCheckedin = candidate.isCheckedIn();
        model.addAttribute("candidate", candidate);
        model.addAttribute("events", events);
        model.addAttribute("isConfirmed",isConfirmed);
        model.addAttribute("isCheckedIn",isCheckedin);
        competitionTMP = candidate.getCompetition();
        return "candidate/edit";
    }

    @PostMapping("/updateCandidate")
    public String updateCandidate(@ModelAttribute("candidate") @Valid RequestUpdateCandidate candidate,
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            candidate.setCompetition(competitionTMP);
            model.addAttribute("errors", errors);
            model.addAttribute("candidate", candidate);
            model.addAttribute("isConfirmed",isConfirmed);
            model.addAttribute("events", events);
            return "candidate/edit";
        }
        candidateService.updateCandidate(candidate);
        return "redirect:/candidates/detail?id=" + candidate.getId();
    }

    @GetMapping("/stat")
    public String getCandidateStatistics(Model model) {
        List<Object[]> statistics = candidateService.getCountCandidatesByEvent();
        model.addAttribute("statistics", statistics);
        return "candidate/stat"; // Tên view bạn muốn render
    }

    @GetMapping("/byEventAndCompetition/{competitionId}/{eventId}")
    public String getCandidatesByEventAndCompetition(
            @PathVariable("competitionId") String competitionId,
            @PathVariable("eventId") String eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String searchType,
            HttpSession httpSession,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.asc("isConfirmed"),
                Sort.Order.desc("registrationTime")
        ));
        List<Candidate> candidates = candidateService.findCandidateByCompetitionIdAndEventId(competitionId, eventId).stream()
                .filter(Candidate::isConfirmed)
                .filter((e) -> e.getCheckinID() != null)
                .collect(Collectors.toList());;
        httpSession.setAttribute("candidates", candidates);
        Page<Candidate> candidatePage = candidateService.findCandidateByCompetitionAndEvent(competitionId, eventId, pageable);
        Event event = eventService.findById(eventId);
        Competition competition = competitionService.getById(competitionId);

        model.addAttribute("candidates", candidatePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", candidatePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("event", event);
        model.addAttribute("competition", competition);
        model.addAttribute("size", size);
        return "candidate/byEventAndCompetition";
    }

}
