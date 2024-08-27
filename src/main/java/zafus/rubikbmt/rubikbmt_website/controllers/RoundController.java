package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Round;
import zafus.rubikbmt.rubikbmt_website.entities.RoundDetail;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;
import zafus.rubikbmt.rubikbmt_website.services.EventService;
import zafus.rubikbmt.rubikbmt_website.services.RoundDetailService;
import zafus.rubikbmt.rubikbmt_website.services.RoundService;

import java.util.List;

@Controller
@RequestMapping("/rounds")
public class RoundController {
    @Autowired
    private RoundService roundService;

    @Autowired
    private CompetitionService competitionService;

    @Autowired
    private EventService eventService;

    @Autowired
    private RoundDetailService roundDetailService;

    @GetMapping("/add/{competitionId}/{eventId}")
    public String add(Model model, @PathVariable("competitionId") String competitionId,
                                   @PathVariable("eventId") String eventId, HttpSession session) {

        model.addAttribute("round", new Round());
        model.addAttribute("event", eventService.findById(eventId));
        model.addAttribute("events", eventService.findAll());
        model.addAttribute("competition", competitionService.getById(competitionId));
        model.addAttribute("competitions", competitionService.getAll());
        return "round/add";
    }

    @PostMapping("/add")
    public String add( @ModelAttribute Round round, HttpSession session) {
        List<Candidate> candidates = (List<Candidate>) session.getAttribute("candidates");
        round.setNumOfCandidate(candidates.size());
        Round newRound = roundService.add(round);
        for (Candidate candidate : candidates) {
            RoundDetail roundDetail = new RoundDetail();
            roundDetail.setRoundId(newRound);
            roundDetail.setCandidate(candidate);
            roundDetailService.add(roundDetail);
        }
        return "round/add";
    }
}
