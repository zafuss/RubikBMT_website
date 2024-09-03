package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;
import zafus.rubikbmt.rubikbmt_website.services.EventService;
import zafus.rubikbmt.rubikbmt_website.services.RoundDetailService;
import zafus.rubikbmt.rubikbmt_website.services.RoundService;

import java.time.Duration;
import java.util.Comparator;
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
    @GetMapping("/addFromPreviousRound/{roundId}")
    public String addFromPreviousRound(Model model,
                                   @PathVariable("roundId") String roundId, HttpSession session) {

        Round currentRound = roundService.findById(roundId);
        Round newRound = new Round();
        newRound.setCompetition(currentRound.getCompetition());
        newRound.setEvent(currentRound.getEvent());
        model.addAttribute("round", new Round());
        model.addAttribute("currentRound", currentRound);
        model.addAttribute("competitions", competitionService.getAll());
        model.addAttribute("events", eventService.findAll());
        return "round/addFromPreviousRound";
    }

    @PostMapping("/add")
    public String add( @ModelAttribute Round round, HttpSession session) {
        List<Candidate> candidates = (List<Candidate>) session.getAttribute("candidates");
        round.setNumOfCandidate(candidates.size());
        Round newRound = roundService.add(round);
        for (Candidate candidate : candidates) {
            RoundDetail roundDetail = new RoundDetail();
            roundDetail.setRound(newRound);
            roundDetail.setCandidate(candidate);
            roundDetailService.add(roundDetail);
        }
        return "redirect:/roundDetails/byRound/" + newRound.getId();
    }

    @PostMapping("/addFromPreviousRound/{roundId}")
    public String addFromBeforeRound( @ModelAttribute Round round, HttpSession session, @PathVariable("roundId") String roundId, Pageable pageable) {
        Round currentRound = roundService.findById(roundId);
        round.setCompetition(currentRound.getCompetition());
        round.setEvent(currentRound.getEvent());
        Round newRound = roundService.add(round);

        List<RoundDetail> roundDetails = roundDetailService.findListByRoundId(roundId);
        List<RoundDetail> sortedCandidates = roundDetails.stream()
                .sorted(Comparator
                        .comparing((RoundDetail rd) -> rd.getAo5() != null && rd.getBest() != null ? 0 : 1)
                        .thenComparing((RoundDetail rd) -> rd.getAo5() != null ? rd.getAo5().getDuration() : Duration.ZERO, Comparator.naturalOrder())
                        .thenComparing((RoundDetail rd) -> rd.getBest() != null ? rd.getBest().getDuration() : Duration.ZERO, Comparator.naturalOrder())
                        .thenComparing(rd -> rd.getCandidate().getFirstName(), RoundDetail.collator))
                .toList();
        for (int i = 0; i < newRound.getNumOfCandidate(); i++) {
            RoundDetail newRoundDetail = new RoundDetail();
            newRoundDetail.setRound(newRound);
            newRoundDetail.setCandidate(sortedCandidates.get(i).getCandidate());
            roundDetailService.add(newRoundDetail);
        }

        return "redirect:/roundDetails/byRound/" + newRound.getId();

    }

    @GetMapping("/byEventAndCompetition/{competitionId}/{eventId}")
    public String getRoundsByEventAndCompetition(
            @PathVariable("competitionId") String competitionId,
            @PathVariable("eventId") String eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String searchType,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.desc("numOfCandidate")
        ));

        Event event = eventService.findById(eventId);
        Competition competition = competitionService.getById(competitionId);
        Page<Round> roundPage = roundService.findRoundByCompetitionAndEventsId(competitionId, eventId, pageable);
        model.addAttribute("rounds", roundPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", roundPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("event", event);
        model.addAttribute("competition", competition);
        model.addAttribute("size", size);
        return "round/byEventAndCompetition";
    }
}
