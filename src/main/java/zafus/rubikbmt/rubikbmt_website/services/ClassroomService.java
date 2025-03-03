package zafus.rubikbmt.rubikbmt_website.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Classroom;
import zafus.rubikbmt.rubikbmt_website.entities.Course;
import zafus.rubikbmt.rubikbmt_website.entities.Teacher;
import zafus.rubikbmt.rubikbmt_website.repositories.IClassroomRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateClassroom;

import java.util.List;

@Service
public class ClassroomService {

    private final IClassroomRepository classroomRepository;

    public ClassroomService(IClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    public Classroom getClassroomById(String id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
    }

    public List<Classroom> getClassroomsByTeacher(Teacher teacher) {
        return classroomRepository.findByTeacher(teacher);
    }

    public List<Classroom> getClassroomsByCourse(Course course) {
        return classroomRepository.findByCourse(course);
    }

    public void saveClassroom(Classroom classroom) {
        classroomRepository.save(classroom);
    }

    public void deleteClassroom(String id) {
        if (!classroomRepository.existsById(id)) {
            throw new EntityNotFoundException("Classroom not found");
        }
        classroomRepository.deleteById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void update(@NotNull RequestUpdateClassroom requestClassroom) {
        try {
            Classroom classroom = classroomRepository.findById(requestClassroom.getId()).orElse(null);

            classroom.setActualFee(requestClassroom.getActualFee());
            classroom.setName(requestClassroom.getName());
            classroom.setDescription(requestClassroom.getDescription());
            classroom.setTeacher(requestClassroom.getTeacher());

            classroomRepository.save(classroom);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Page<Classroom> searchClassroom(String keyword, String searchType, Pageable pageable) {
        switch (searchType) {
            case "course":
                return classroomRepository.findClassroomByCourseContaining(keyword, pageable);
            case "teacher":
                return classroomRepository.findClassroomsByTeacherContaining(keyword, pageable);
            default:
                return classroomRepository.findAllClassroom(pageable);
        }
    }
}