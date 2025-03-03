package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zafus.rubikbmt.rubikbmt_website.entities.Classroom;
import zafus.rubikbmt.rubikbmt_website.entities.Course;
import zafus.rubikbmt.rubikbmt_website.entities.Student;
import zafus.rubikbmt.rubikbmt_website.entities.Teacher;

import java.util.List;

@Repository
public interface IClassroomRepository extends JpaRepository<Classroom, String> {
    List<Classroom> findByTeacher(Teacher teacher);
    List<Classroom> findByCourse(Course course);
    @Query("SELECT t FROM Classroom t WHERE t.course.name LIKE %:courseName%")
    Page<Classroom> findClassroomByCourseContaining(@Param("course") String courseName, Pageable pageable);

    @Query("SELECT t FROM Classroom t WHERE t.teacher.lastName LIKE %:teacherName%")
    Page<Classroom> findClassroomsByTeacherContaining(@Param("teacher") String teacherName, Pageable pageable);

    @Query("SELECT t FROM Classroom t")
    Page<Classroom> findAllClassroom(Pageable pageable);
}
