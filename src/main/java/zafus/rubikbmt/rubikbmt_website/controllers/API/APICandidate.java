package zafus.rubikbmt.rubikbmt_website.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zafus.rubikbmt.rubikbmt_website.DTO.CandidateDTO;
import zafus.rubikbmt.rubikbmt_website.DTO.EventDTO;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apiCheck/candidates")
public class APICandidate {
    @Autowired
    private CandidateService candidateService;
    @GetMapping("/accept")
    public ResponseEntity<Map<String, Object>> getCandidates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        if (page < 0 || size <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Candidate> candidatesPage = candidateService.findUnconfirmedCandidates(pageable,size);

        List<CandidateDTO> candidateDTOs = candidatesPage.getContent().stream()
                .map(candidate -> new CandidateDTO(
                        candidate.getFullName(),
                        candidate.getPhoneNumber(),
                        candidate.getEvents().stream()
                                .map(event -> new EventDTO(event.getName()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("candidates", candidateDTOs);
        response.put("currentPage", candidatesPage.getNumber());
        response.put("totalPages", candidatesPage.getTotalPages());
        response.put("totalCandidates", candidatesPage.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
