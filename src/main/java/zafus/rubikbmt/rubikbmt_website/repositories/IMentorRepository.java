package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zafus.rubikbmt.rubikbmt_website.entities.Mentor;

public interface IMentorRepository extends JpaRepository<Mentor, String> {
}

