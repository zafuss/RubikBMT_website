package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;

public interface ICompetitionRepository extends JpaRepository<Competition, String> {
    public Competition findByName(String name);
}
