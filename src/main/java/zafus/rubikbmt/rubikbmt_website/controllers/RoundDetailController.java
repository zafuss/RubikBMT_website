package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Round;
import zafus.rubikbmt.rubikbmt_website.entities.RoundDetail;
import zafus.rubikbmt.rubikbmt_website.services.RoundDetailService;
import zafus.rubikbmt.rubikbmt_website.services.RoundService;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static zafus.rubikbmt.rubikbmt_website.entities.RoundDetail.collator;

@Controller
@RequestMapping("/roundDetails")
public class RoundDetailController {

    @Autowired
    private RoundService roundService;

    @Autowired
    private RoundDetailService roundDetailService;

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
                .sorted((c1, c2) -> collator.compare(c1.getCandidate().getFirstName(), c2.getCandidate().getFirstName()))
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
