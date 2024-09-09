package zafus.rubikbmt.rubikbmt_website.controllers.API;

import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.DTO.RoundDTO;
import zafus.rubikbmt.rubikbmt_website.DTO.RoundDetailDTO;
import zafus.rubikbmt.rubikbmt_website.DTO.SolvesDTO;
import zafus.rubikbmt.rubikbmt_website.IO.TimeComponent;
import zafus.rubikbmt.rubikbmt_website.entities.Round;
import zafus.rubikbmt.rubikbmt_website.entities.RoundDetail;
import zafus.rubikbmt.rubikbmt_website.entities.Solve;
import zafus.rubikbmt.rubikbmt_website.services.RoundDetailService;
import zafus.rubikbmt.rubikbmt_website.services.RoundService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static zafus.rubikbmt.rubikbmt_website.entities.RoundDetail.collator;

@RestController
@RequestMapping("/apiRoundBy")
public class APIRound {
    @Autowired
    private RoundDetailService roundDetailService;
    @Autowired
    private RoundService roundService;

    @PostMapping("/Event")
    public ResponseEntity<?> getCandidatesByRoundAndEvent(@RequestBody Map<String, Object> data) {
        Integer page = (Integer) data.get("page");
        String roundDetailId = (String) data.get("roundDetailId");
        int size = 200;

        if (page < 0 || size <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(page, size);
        Round round = roundService.findById(roundDetailId);

        Page<RoundDetail> roundDetailsPages = roundDetailService.findAllByRoundIdAndLimit(round.getId(),
                round.getNumOfCandidate(), pageable);

        List<RoundDetailDTO> roundDetailDTOS = roundDetailsPages.getContent().stream()
                .map(roundDetail -> new RoundDetailDTO(
                        roundDetail.getDurationString("best"),
                        roundDetail.getDurationString("avg"),
                        roundDetail.getDurationString("ao5"),
                        roundDetail.getDurationString("worst"),
                        roundDetail.getSolves().stream()
                                .sorted(Comparator.comparingInt(Solve::getOrderIndex))
                                .map(solve -> new SolvesDTO(
                                        solve.getDurationString(),
                                        solve.isDNF(),
                                        solve.getOrderIndex()
                                ))
                                .collect(Collectors.toList()),
                        roundDetail.getCandidate().getFullName(),
                        roundDetail.getRankRound(),
                        roundDetail.getCandidate().getFirstName(),
                        roundDetail.getRound().getNextRoundCandidate(),
                        roundDetail.getCandidate().getCheckinID() == null ? 0 : roundDetail.getCandidate().getCheckinID()))
                .collect(Collectors.toList());

        // Sắp xếp danh sách theo rankRound, đưa các phần tử có rankRound = 0 xuống dưới
        roundDetailDTOS.sort((dto1, dto2) -> {
            if (dto1.getRankRound() == 0 && dto2.getRankRound() == 0) {
                // Nếu cả hai đều có rankRound = 0, sắp xếp theo firstName
                return collator.compare(dto1.getFirstName(), dto2.getFirstName());
            } else if (dto1.getRankRound() == 0) {
                return 1;  // Đưa dto1 xuống dưới
            } else if (dto2.getRankRound() == 0) {
                return -1;  // Đưa dto2 xuống dưới
            } else {
                return Integer.compare(dto1.getRankRound(), dto2.getRankRound());
            }
        });

        Map<String, Object> response = new HashMap<>();
        response.put("roundDetail", roundDetailDTOS);
        response.put("currentPage", roundDetailsPages.getNumber());
        response.put("totalPages", roundDetailsPages.getTotalPages());
        response.put("totalCandidates", roundDetailsPages.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/allEvent")
    public ResponseEntity<?> getRound(
            @RequestBody Map<String, Object> data){
        String competitionId = (String) data.get("competitionId");
        String eventId = (String) data.get("eventId");
        List<RoundDTO> roundList = roundService.findRoundByCompetitionAndEventsId(competitionId, eventId).stream()
                .map(roundDTO -> new RoundDTO(
                        roundDTO.getCompetition().getId(),
                        roundDTO.getEvent().getId(),
                        roundDTO.getId(),
                        roundDTO.getName(),
                        roundDTO.getCreateTime()
                )).sorted(Comparator.comparing(RoundDTO::getCreateTime).reversed()
                ).collect(Collectors.toList());
        return new ResponseEntity<>(roundList, HttpStatus.OK);
    }
}
