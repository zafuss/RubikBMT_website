package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.Teacher;
import zafus.rubikbmt.rubikbmt_website.entities.User;

public interface ITeacherRepository extends JpaRepository<Teacher, String> {
    @Query("SELECT t FROM Teacher t WHERE t.email LIKE %:email%")
    Page<Teacher> findTeachersByEmailContaining(@Param("email") String email, Pageable pageable);

    @Query("SELECT t FROM Teacher t WHERE t.phoneNumber LIKE %:phoneNumber%")
    Page<Teacher> findTeachersByPhoneNumberContaining(@Param("phoneNumber") String phoneNumber, Pageable pageable);

    @Query("SELECT t FROM Teacher t")
    Page<Teacher> findAllTeachers(Pageable pageable);
}
