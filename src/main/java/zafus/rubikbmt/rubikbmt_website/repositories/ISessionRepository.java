package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zafus.rubikbmt.rubikbmt_website.entities.Classroom;
import zafus.rubikbmt.rubikbmt_website.entities.Session;
import zafus.rubikbmt.rubikbmt_website.entities.Teacher;

import java.util.List;

public interface ISessionRepository extends JpaRepository<Session, String> {
    List<Session> findByClassroomTeacher(Teacher teacher);
    List<Session> findByClassroom(Classroom classroom);

}