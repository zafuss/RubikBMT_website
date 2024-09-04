package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.Round;
import zafus.rubikbmt.rubikbmt_website.entities.RoundDetail;

import java.util.List;

public interface IRoundDetailRepository extends JpaRepository<RoundDetail, String> {
    //    @Query("SELECT c FROM RoundDetail c WHERE c.roundId.id = :roundId ORDER BY COALESCE(c.avg.duration, c.best.duration, 'P9999D') ASC, c.id ASC")
    @Query("SELECT c FROM RoundDetail c WHERE c.round.id = :roundId")
    Page<RoundDetail> findByRoundId(@Param("roundId") String roundId, Pageable pageable);

    @Query("SELECT c FROM RoundDetail c WHERE c.round.id = :roundId " +
            "AND c.rankRound BETWEEN 0 AND :limitCandidate " +
            "ORDER BY c.rankRound ASC ")
    Page<RoundDetail> findByRoundIdAndLimit(@Param("roundId") String roundId,
                                            @Param("limitCandidate") int limitCandidate,
                                            Pageable pageable);


    @Query("SELECT c FROM RoundDetail c WHERE c.round.id = :roundId")
    List<RoundDetail> findListByRoundId(@Param("roundId") String roundId);

    @Query("select c from RoundDetail c where c.round.id = :roundId and c.candidate.id = :candidateId")
    RoundDetail findRoundDetailByCandidateAndRound(@Param("roundId") String roundId, @Param("candidateId") String candidateId);


}


