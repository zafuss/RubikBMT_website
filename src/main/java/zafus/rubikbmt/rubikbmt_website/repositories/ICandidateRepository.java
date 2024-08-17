package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;

public interface ICandidateRepository extends JpaRepository<Candidate, String> {
}
