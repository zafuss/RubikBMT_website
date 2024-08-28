package zafus.rubikbmt.rubikbmt_website.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Round;
import zafus.rubikbmt.rubikbmt_website.entities.RoundDetail;
import zafus.rubikbmt.rubikbmt_website.repositories.IRoundDetailRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class RoundDetailService {
    private final IRoundDetailRepository roundDetailRepository;

    public List<RoundDetail> findAll() {
        return roundDetailRepository.findAll();
    }

    public Page<RoundDetail> findByRoundId(String roundId, Pageable pageable) {
        return roundDetailRepository.findByRoundId(roundId, pageable);
    }
    public List<RoundDetail> findListByRoundId(String roundId) {
        return roundDetailRepository.findListByRoundId(roundId);
    }

    public RoundDetail findById(String id) {
        return roundDetailRepository.findById(id).orElse(null);
    }

    public RoundDetail add(RoundDetail roundDetail) {
        return roundDetailRepository.save(roundDetail);
    }

    public RoundDetail update(RoundDetail roundDetail) {
        return roundDetailRepository.save(roundDetail);
    }

    public void delete(RoundDetail roundDetail) {
        roundDetailRepository.delete(roundDetail);
    }


}