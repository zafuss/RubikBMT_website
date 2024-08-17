package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zafus.rubikbmt.rubikbmt_website.entities.Event;
import zafus.rubikbmt.rubikbmt_website.entities.User;

public interface IUserRepository extends JpaRepository<User, String> {
    User findFirstById(String id);

    User findUserByUserName(String username);
    User findUserByEmail(String email);
    User findUserByPhoneNumber(String phoneNumber);
}

