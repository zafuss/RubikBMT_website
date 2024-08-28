package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Round;
import zafus.rubikbmt.rubikbmt_website.repositories.IRoundRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class RoundService {
    private final IRoundRepository roundRepository;

    public List<Round> findAll() {
        return roundRepository.findAll();
    }

    public Round findById(String id) {
        return roundRepository.findById(id).orElse(null);
    }

    public Round add(Round round) {
        return roundRepository.save(round);
    }

    public Round update(Round round) {
        return roundRepository.save(round);
    }

    public void delete(Round round) {
        roundRepository.delete(round);
    }

    public Page<Round> findRoundByCompetitionAndEventsId(String competitionId, String eventsId, Pageable pageable) {
        return roundRepository.findByCompetitionIdAndEventsId(competitionId, eventsId, pageable);
    }

}