package zafus.rubikbmt.rubikbmt_website.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import zafus.rubikbmt.rubikbmt_website.repositories.IClassroomDetailRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateClassroomDetail;

import java.util.List;

@Service
public class ClassroomDetailService {

    @Autowired
    private IClassroomDetailRepository classroomDetailRepository;

    public List<ClassroomDetail> getAllClassroomDetails() {
        return classroomDetailRepository.findAll();
    }

    public ClassroomDetail getClassroomDetailById(String id) {
        return classroomDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ClassroomDetail not found"));
    }

    public List<ClassroomDetail> getClassroomDetailsByClassroom(Classroom classroom) {
        return classroomDetailRepository.findByClassroom(classroom);
    }

    public List<ClassroomDetail> getClassroomDetailsByStudent(Student student) {
        return classroomDetailRepository.findByStudent(student);
    }

    public void saveClassroomDetail(ClassroomDetail classroomDetail) {
        classroomDetailRepository.save(classroomDetail);
    }

    public void deleteClassroomDetail(String id) {
        if (!classroomDetailRepository.existsById(id)) {
            throw new EntityNotFoundException("ClassroomDetail not found");
        }
        classroomDetailRepository.deleteById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void update(@NotNull RequestUpdateClassroomDetail requestClassroomDetail) {
        try {
            ClassroomDetail classroomDetail = classroomDetailRepository.findById(requestClassroomDetail.getId()).orElse(null);

            classroomDetail.setClassroom(requestClassroomDetail.getClassroom());
            classroomDetail.setStudent(requestClassroomDetail.getStudent());
            classroomDetail.setId(requestClassroomDetail.getId());
            classroomDetail.setPaid(requestClassroomDetail.getPaid());

            classroomDetailRepository.save(classroomDetail);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Page<ClassroomDetail> searchClassroomDetail(String keyword, String searchType, Pageable pageable) {
        switch (searchType) {
            case "student":
                return classroomDetailRepository.findClassroomByStudentContaining(keyword, pageable);
            case "classroom":
                return classroomDetailRepository.findClassroomByClassroomContaining(keyword, pageable);
            default:
                return classroomDetailRepository.findAllClassroomDetail(pageable);
        }
    }
}