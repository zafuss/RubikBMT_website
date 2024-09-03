package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Round;

import java.util.List;

public interface IRoundRepository extends JpaRepository<Round, String> {
    @Query("SELECT c FROM Round c  WHERE c.competition.id = :competitionId AND c.event.id = :eventId")
    Page<Round> findByCompetitionIdAndEventsId(@Param("competitionId") String competitionId,
                                               @Param("eventId") String eventId, Pageable pageable);

    @Query("SELECT c FROM Round c  WHERE c.competition.id = :competitionId AND c.event.id = :eventId")
    List<Round> findByCompetitionIdAndEventsId(@Param("competitionId") String competitionId,
                                               @Param("eventId") String eventId);


}

