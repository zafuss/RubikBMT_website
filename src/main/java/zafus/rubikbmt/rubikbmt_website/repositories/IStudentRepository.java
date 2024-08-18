package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Role;
import zafus.rubikbmt.rubikbmt_website.entities.Student;

import java.util.List;

public interface IStudentRepository extends JpaRepository<Student, String> {
    Page<Student> findByEmailContainingOrPhoneNumberContaining(
            String email, String phoneNumber, Pageable pageable);
    List<Student> findByEmailContaining(String email);

    List<Student> findByPhoneNumberContaining(String phoneNumber);
}

