package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.validation.Valid;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.IO.ConvertToDuration;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Round;
import zafus.rubikbmt.rubikbmt_website.entities.RoundDetail;
import zafus.rubikbmt.rubikbmt_website.entities.Solve;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateSolve;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateCandidate;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.RoundDetailService;
import zafus.rubikbmt.rubikbmt_website.services.RoundService;
import zafus.rubikbmt.rubikbmt_website.services.SolveService;

import java.text.Collator;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static zafus.rubikbmt.rubikbmt_website.entities.RoundDetail.*;

@Controller
@RequestMapping("/roundDetails")
public class RoundDetailController {

    @Autowired
    private RoundService roundService;

    @Autowired
    private RoundDetailService roundDetailService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private SolveService solveService;


    @GetMapping("/editCandidate")
    public String roundEdit(Model model, @RequestParam("id1") String roundID,@RequestParam("id2") String candidateID ) {
//        Candidate candidate = candidateService.findById(candidateId);
        RequestCreateSolve requestCreateSolve = new RequestCreateSolve();
        requestCreateSolve.setRoundId(roundID);
        requestCreateSolve.setCandidateId(candidateID);
//        model.addAttribute("candidate",candidate);
        model.addAttribute("requestCreateSolve", requestCreateSolve);
        return "roundDetail/edit";
    }

    @PostMapping("/updateCandidate")
    public String updateDetail(@Valid @ModelAttribute RequestCreateSolve requestCreateSolve, BindingResult bindingResult, Model model) {
        try {
            Solve solve;
            String roundId = requestCreateSolve.getRoundId();
            String candidateId = requestCreateSolve.getCandidateId();
            RoundDetail roundDetail = roundDetailService.findRoundDetailByCandidateAndRound(roundId, candidateId);
            List<Solve> solves = new ArrayList<>();
            Duration duration ;
            for (int i = 1; i <= 5; i++) {
                if (requestCreateSolve.getSolve(i).equals("out")){
                    duration = Solve.DNF_DURATION;
                    solve = new Solve();
                    solve.setDNF(true);
                    solve.setDuration(duration);
                    solve.setDurationString(duration.toString());
                }
                else{
                    String formatted = ConvertToDuration.convertToFormat(requestCreateSolve.getSolve(i));
                    duration = ConvertToDuration.parseDuration(formatted);
                    solve = new Solve();
                    solve.setDuration(duration);
                    solve.setDurationString(duration.toString());
                }
                Solve solvetmp = solveService.add(solve);
                solves.add(solvetmp);
            }
            roundDetail.setSolves(solves);
            roundDetail.setBest(findBest(solves));
            roundDetail.setWorst(findWorst(solves));
            Solve solveAvg = findAvg(solves);
            solveAvg.setDurationString(solveAvg.getDuration().toString());
            Solve solvetmpAvg = solveService.add(solveAvg);
            roundDetail.setAvg(solvetmpAvg);
            Solve solveAo5 = findAo5(solves);
            solveAo5.setDurationString(solveAo5.getDuration().toString());
            Solve solvetmpAo5 = solveService.add(solveAo5);
            roundDetail.setAo5(solvetmpAo5);
            roundDetailService.update(roundDetail);
            return "redirect:/roundDetails/byRound/" + roundId;
        } catch (Exception ex) {
            throw new RuntimeException("loi add");
        }
    }

    @GetMapping("/byRound/{roundId}")
    public String byRound(Model model,
                          @PathVariable("roundId") String roundId,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "100") int size,
                          @RequestParam(defaultValue = "") String keyword,
                          @RequestParam(defaultValue = "") String searchType) {

//        Pageable pageable = PageRequest.of(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.asc("candidate.firstName")
                // Sắp xếp theo avg.duration tăng dần
        ));
        Page<RoundDetail> roundDetailPage = roundDetailService.findByRoundId(roundId, pageable);
        List<RoundDetail> sortedCandidates = roundDetailPage.getContent().stream()
                .sorted(Comparator
                        .comparing((RoundDetail rd) -> rd.getAvg() != null && rd.getBest() != null ? 0 : 1)
                        .thenComparing((RoundDetail rd) -> rd.getAvg() != null ? rd.getAvg().getDuration() : Duration.ZERO, Comparator.naturalOrder())
                        .thenComparing((RoundDetail rd) -> rd.getBest() != null ? rd.getBest().getDuration() : Duration.ZERO, Comparator.naturalOrder())
                        .thenComparing(rd -> rd.getCandidate().getFirstName(), Comparator.naturalOrder()))
                .toList();

        model.addAttribute("roundDetails", sortedCandidates);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", roundDetailPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("roundId", roundId);
        model.addAttribute("size", size);

        return "roundDetail/byRound";
    }
}
