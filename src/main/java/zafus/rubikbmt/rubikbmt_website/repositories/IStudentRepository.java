package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.Student;

import java.time.LocalDate;

public interface IStudentRepository extends JpaRepository<Student, String> {
    @Query("SELECT t FROM Student t WHERE t.email LIKE %:email%")
    Page<Student > findStudentsByEmailContaining(@Param("email") String email, Pageable pageable);

    @Query("SELECT t FROM Student t WHERE t.phoneNumber LIKE %:phoneNumber%")
    Page<Student> findStudentsByPhoneNumberContaining(@Param("phoneNumber") String phoneNumber, Pageable pageable);

    @Query("SELECT t FROM Student t")
    Page<Student> findAllStudents(Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO student (birthday, studied_course, parent_name, parent_phone_number, id) " +
            "VALUES (:birthday, :studiedCourse, :parentName, :parentPhoneNumber, :id)", nativeQuery = true)
    void insertStudent(@Param("birthday") LocalDate birthday,
                       @Param("studiedCourse") int studiedCourse,
                       @Param("parentName") String parentName,
                       @Param("parentPhoneNumber") String parentPhoneNumber,
                       @Param("id") String userId);
}
