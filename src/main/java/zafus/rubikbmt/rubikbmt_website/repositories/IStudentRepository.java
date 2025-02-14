package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.RegisterStudent;

import java.util.List;

public interface IStudentRepository extends JpaRepository<RegisterStudent, String> {
    Page<RegisterStudent> findByEmailContaining(String email, Pageable pageable);

    Page<RegisterStudent> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);

    Page<RegisterStudent> findByFullNameContaining(String fullName, Pageable pageable);

    List<RegisterStudent> findByEmailContaining(String email);

    List<RegisterStudent> findByPhoneNumberContaining(String phoneNumber);

    List<RegisterStudent> findByLastNameContainingOrFirstNameContaining(String lastName, String firstName);

    @Query("SELECT s FROM RegisterStudent s WHERE s.email = :email")
    RegisterStudent findByEmail(@Param("email") String email);
    RegisterStudent findByPhoneNumber(String phoneNumber);
}

