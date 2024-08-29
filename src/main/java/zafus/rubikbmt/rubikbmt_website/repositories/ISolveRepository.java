package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zafus.rubikbmt.rubikbmt_website.entities.Round;
import zafus.rubikbmt.rubikbmt_website.entities.Solve;

public interface ISolveRepository extends JpaRepository<Solve, String> {

}
