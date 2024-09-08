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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static zafus.rubikbmt.rubikbmt_website.entities.RoundDetail.*;

@Controller
@RequestMapping("/roundDetails")
@SessionAttributes("roundDetail")
public class RoundDetailController {

    @Autowired
    private RoundService roundService;

    @Autowired
    private RoundDetailService roundDetailService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private SolveService solveService;

    private RoundDetail round;


    @GetMapping("/addCandidate")
    public String roundAdd(Model model, @RequestParam("id1") String roundID, @RequestParam("id2") String candidateID) {
//        Candidate candidate = candidateService.findById(candidateId);
        RequestCreateSolve requestCreateSolve = new RequestCreateSolve();
        requestCreateSolve.setRoundId(roundID);
        requestCreateSolve.setCandidateId(candidateID);
        Candidate candidate = candidateService.findById(candidateID);
        model.addAttribute("candidate",candidate);
        model.addAttribute("requestCreateSolve", requestCreateSolve);
        return "roundDetail/add";
    }

    @PostMapping("/updateCandidate")
    public String updateDetail(@Valid @ModelAttribute RequestCreateSolve requestCreateSolve,
                               BindingResult bindingResult, Model model) {
        try {
            String roundId = requestCreateSolve.getRoundId();
            String candidateId = requestCreateSolve.getCandidateId();
            RoundDetail roundDetail = roundDetailService.findRoundDetailByCandidateAndRound(roundId, candidateId);
            List<Solve> solves = new ArrayList<>();
            processValue(requestCreateSolve, roundDetail, solves);
            return "redirect:/roundDetails/byRound/" + roundId;
        } catch (Exception ex) {
            throw new RuntimeException("loi add");
        }
    }

    @GetMapping("/editCandidate")
    public String roundEdit(Model model, @RequestParam("id1") String roundID,
                            @RequestParam("id2") String candidateID) {
        try {
            RequestCreateSolve requestCreateSolve = new RequestCreateSolve();
            RoundDetail roundDetail = roundDetailService.findRoundDetailByCandidateAndRound(roundID, candidateID);

            Candidate candidate = candidateService.findById(candidateID);
            requestCreateSolve.setRoundId(roundID);
            requestCreateSolve.setCandidateId(candidateID);
            if (roundDetail == null) {
                return "redirect:/roundDetails/byRound/" + roundID;
            } else if (roundDetail.getSolves().isEmpty()) {
                model.addAttribute("requestCreateSolve", requestCreateSolve);
            } else {
                List<Solve> solves = roundDetail.getSolves().stream()
                        .sorted(Comparator.comparingInt(Solve::getOrderIndex)).toList();
                for (int i = 0; i < solves.size(); i++) {
                    String durationString = solves.get(i).getDurationString();
                    if (durationString.equals("PT10H")) {
                        durationString = "DNF";
                    } else {
                        System.out.println(durationString);
                        durationString = ConvertToDuration.convertToNumber(durationString);
                    }
                    switch (i) {
                        case 0:
                            requestCreateSolve.setSolve1(durationString);
                            break;
                        case 1:
                            requestCreateSolve.setSolve2(durationString);
                            break;
                        case 2:
                            requestCreateSolve.setSolve3(durationString);
                            break;
                        case 3:
                            requestCreateSolve.setSolve4(durationString);
                            break;
                        case 4:
                            requestCreateSolve.setSolve5(durationString);
                            break;
                        default:
                            break;
                    }
                }
                model.addAttribute("requestCreateSolve", requestCreateSolve);
                model.addAttribute("candidate",candidate);
                round = roundDetail;
            }

            return "roundDetail/edit";
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi trong quá trình xử lý: " + ex.getMessage(), ex);
        }
    }

    @PostMapping("/updateAddCandidate")
    public String addCandidate(@Valid @ModelAttribute RequestCreateSolve requestCreateSolve,
                               BindingResult bindingResult, Model model) {
        try {
            String roundId = requestCreateSolve.getRoundId();
            RoundDetail roundDetail = round;
            List<Solve> solves = new ArrayList<>();
            if (!roundDetail.getSolves().isEmpty()) {
                solves = roundDetail.getSolves();
                processValueEdit(requestCreateSolve, roundDetail, solves);
            } else {
                processValue(requestCreateSolve, roundDetail, solves);
            }

            return "redirect:/roundDetails/byRound/" + roundId;
        } catch (Exception ex) {
            throw new RuntimeException("loi update");
        }
    }

    private void processValueEdit(@ModelAttribute @Valid RequestCreateSolve requestCreateSolve, RoundDetail roundDetail, List<Solve> solves) {
        Duration duration;
        Solve solve;
        for (int i = 1; i <= 5; i++) {
            if (requestCreateSolve.getSolve(i).equalsIgnoreCase("dnf")) {
                duration = Solve.DNF_DURATION;
                solve = solves.get(i - 1);
                solve.setDNF(true);
                solve.setDuration(duration);
                solve.setDurationString(duration.toString());
                solve.setOrderIndex(i);
            } else {
                String formatted = ConvertToDuration.convertToFormat(requestCreateSolve.getSolve(i));
                duration = ConvertToDuration.parseDuration(formatted);
                solve = solves.get(i - 1);
                solve.setDuration(duration);
                solve.setDNF(false);
                solve.setDurationString(duration.toString());
                solve.setOrderIndex(i);
            }
        }
        saveRoundDetail(roundDetail, solves);
    }

    private void saveRoundDetail(RoundDetail roundDetail, List<Solve> solves) {
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
    }

    private void processValue(@ModelAttribute @Valid RequestCreateSolve requestCreateSolve, RoundDetail roundDetail, List<Solve> solves) {
        Duration duration;
        Solve solve;
        for (int i = 1; i <= 5; i++) {
            if (requestCreateSolve.getSolve(i).equalsIgnoreCase("dnf")) {
                duration = Solve.DNF_DURATION;
                solve = new Solve();
                solve.setDNF(true);
                solve.setDuration(duration);
                solve.setDurationString(duration.toString());
                solve.setOrderIndex(i);
            } else {
                String formatted = ConvertToDuration.convertToFormat(requestCreateSolve.getSolve(i));
                duration = ConvertToDuration.parseDuration(formatted);
                solve = new Solve();
                solve.setDuration(duration);
                solve.setDNF(false);
                solve.setDurationString(duration.toString());
                solve.setOrderIndex(i);
            }
            Solve solvetmp = solveService.add(solve);
            solves.add(solvetmp);
        }
        saveRoundDetail(roundDetail, solves);
    }

    @GetMapping("/byRound/{roundId}")
    public String byRound(Model model,
                          @PathVariable("roundId") String roundId,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "200") int size,
                          @RequestParam(defaultValue = "") String keyword,
                          @RequestParam(defaultValue = "") String searchType) {

        final int[] index = {1};
//        Pageable pageable = PageRequest.of(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.asc("candidate.firstName")
                // Sắp xếp theo avg.duration tăng dần
        ));
        Page<RoundDetail> roundDetailPage = roundDetailService.findByRoundId(roundId, pageable);
        AtomicReference<Duration> previousAo5 = new AtomicReference<>(null);
        AtomicReference<Duration> previousBest = new AtomicReference<>(null);
        AtomicInteger previousRankRound = new AtomicInteger(-1);

        List<RoundDetail> sortedCandidates = roundDetailPage.getContent().stream()
                .sorted(Comparator
                        .comparing((RoundDetail rd) -> rd.getAo5() != null && rd.getBest() != null ? 0 : 1)
                        .thenComparing((RoundDetail rd) -> rd.getAo5() != null ? rd.getAo5().getDuration() : Duration.ZERO, Comparator.naturalOrder())
                        .thenComparing((RoundDetail rd) -> rd.getBest() != null ? rd.getBest().getDuration() : Duration.ZERO, Comparator.naturalOrder())
                        .thenComparing(rd -> rd.getCandidate().getFirstName(), collator))
                .map(rd -> {
                    if (rd.getAo5() != null) {
                        Duration currentAo5 = rd.getAo5().getDuration();
                        Duration currentBest = rd.getBest().getDuration();

                        if (previousAo5.get() != null && previousAo5.get().equals(currentAo5) && previousBest.get().equals(currentBest)) {
                            rd.setRankRound(previousRankRound.get());
                            index[0] += 1;
                        } else {
                            rd.setRankRound(index[0]);
                            previousRankRound.set(index[0]);
                            index[0] += 1;
                        }

                        previousAo5.set(currentAo5);
                        previousBest.set(currentBest);
                    }
                    return rd;
                })
                .toList();

        roundDetailService.saveAll(sortedCandidates);
        model.addAttribute("roundDetails", sortedCandidates);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", roundDetailPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("roundId", roundId);
        model.addAttribute("size", size);

        return "roundDetail/byRound";
    }
}
