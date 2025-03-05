package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zafus.rubikbmt.rubikbmt_website.entities.LevelRating;

@Repository
public interface ILevelRatingRepository extends JpaRepository<LevelRating, String> {
}
