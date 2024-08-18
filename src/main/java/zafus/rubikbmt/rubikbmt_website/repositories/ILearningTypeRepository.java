package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zafus.rubikbmt.rubikbmt_website.entities.LearningType;
import zafus.rubikbmt.rubikbmt_website.entities.Role;

public interface ILearningTypeRepository extends JpaRepository<LearningType, String> {
}

