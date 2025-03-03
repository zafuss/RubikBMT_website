package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.Classroom;
import zafus.rubikbmt.rubikbmt_website.entities.ClassroomDetail;
import zafus.rubikbmt.rubikbmt_website.entities.Student;

import java.util.List;

public interface IClassroomDetailRepository extends JpaRepository<ClassroomDetail, String> {
//    @Query("SELECT t FROM ClassroomDetail t WHERE t.classroom.id = :classroomId")
//    Page<ClassroomDetail> findAllByClassroom(Pageable pageable, @Param("classroomId") String classroomId);

    @Query("SELECT t FROM ClassroomDetail t")
    Page<ClassroomDetail> findAllClassroomDetail(Pageable pageable);

    @Query("SELECT t FROM ClassroomDetail t WHERE t.classroom.id LIKE %:classroomId%")
    Page<ClassroomDetail> findClassroomByClassroomContaining(@Param("classroomId") String classroomId, Pageable pageable);

    @Query("SELECT t FROM ClassroomDetail t WHERE t.student.firstName LIKE %:studentName% OR t.student.lastName LIKE %:studentName%")
    Page<ClassroomDetail> findClassroomByStudentContaining(@Param("studentName") String studentName, Pageable pageable);

    List<ClassroomDetail> findByClassroom(Classroom classroom);
    List<ClassroomDetail> findByStudent(Student student);
}

