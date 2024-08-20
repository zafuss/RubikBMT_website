package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Role;
import zafus.rubikbmt.rubikbmt_website.entities.Student;

import java.util.List;

public interface IStudentRepository extends JpaRepository<Student, String> {
    Page<Student> findByEmailContaining(String email, Pageable pageable);

    Page<Student> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);

    Page<Student> findByFullNameContaining(String fullName, Pageable pageable);

    List<Student> findByEmailContaining(String email);

    List<Student> findByPhoneNumberContaining(String phoneNumber);

    List<Student> findByLastNameContainingOrFirstNameContaining(String lastName, String firstName);

    @Query("SELECT s FROM Student s WHERE s.email = :email")
    Student findByEmail(@Param("email") String email);
    Student findByPhoneNumber(String phoneNumber);
}

