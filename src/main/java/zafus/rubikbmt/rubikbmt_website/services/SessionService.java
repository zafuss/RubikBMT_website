package zafus.rubikbmt.rubikbmt_website.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import zafus.rubikbmt.rubikbmt_website.repositories.ISessionRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateClassroom;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateSession;

import java.util.List;

@Service
public class SessionService {

    private final ISessionRepository sessionRepository;

    public SessionService(ISessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void update(@NotNull RequestUpdateSession requestUpdateSession) {
        try {
            Session session = sessionRepository.findById(requestUpdateSession.getId()).orElse(null);

            session.setContent(requestUpdateSession.getContent());
            session.setStartDate(requestUpdateSession.getStartDate());
            session.setEndDate(requestUpdateSession.getEndDate());

            sessionRepository.save(session);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Session> getSessionsByClassroom(Classroom classroom) {
        return sessionRepository.findByClassroom(classroom);
    }
    public List<Session> getSessionsByTeacher(Teacher teacher) {
        return sessionRepository.findByClassroomTeacher(teacher);
    }

    public Session getSessionById(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
    }

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public void deleteSession(String id) {
        if (!sessionRepository.existsById(id)) {
            throw new EntityNotFoundException("Session not found");
        }
        sessionRepository.deleteById(id);
    }

//    public Page<Session> searchSession(String keyword, String searchType, Pageable pageable) {
//        switch (searchType) {
//            case "course":
//                return sessionRepository.findSessionByCourseContaining(keyword, pageable);
//            case "teacher":
//                return sessionRepository.findSessionsByTeacherContaining(keyword, pageable);
//            default:
//                return sessionRepository.findAllSession(pageable);
//        }
//    }
}