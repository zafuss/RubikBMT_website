package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.Teacher;
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.entities.Event;
import zafus.rubikbmt.rubikbmt_website.entities.User;

import java.util.List;

public interface IUserRepository extends JpaRepository<User, String> {

    Page<User> findByEmailContaining(String email, Pageable pageable);

    Page<User> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);


    List<User> findByEmailContaining(String email);

    List<User> findByPhoneNumberContaining(String phoneNumber);

    List<User> findByLastNameContainingOrFirstNameContaining(String lastName, String firstName);
    //    Page<User> findAll(Specification<User> spec, Pageable pageable);
//    @Query("SELECT c FROM User c WHERE c.isConfirmed = true ORDER BY c.timeConfirmed DESC")
    User findFirstById(String id);

    User findUserByUserName(String username);
    User findUserByEmail(String email);
    User findUserByPhoneNumber(String phoneNumber);

}

