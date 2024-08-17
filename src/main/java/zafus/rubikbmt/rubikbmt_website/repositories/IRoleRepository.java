package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zafus.rubikbmt.rubikbmt_website.entities.Event;
import zafus.rubikbmt.rubikbmt_website.entities.Role;

public interface IRoleRepository extends JpaRepository<Role, String> {
}

