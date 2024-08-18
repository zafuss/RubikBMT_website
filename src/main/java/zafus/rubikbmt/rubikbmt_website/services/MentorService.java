package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Mentor;
import zafus.rubikbmt.rubikbmt_website.repositories.IMentorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class MentorService {
    private final IMentorRepository mentorRepository;

    public List<Mentor> findAll() {
        return mentorRepository.findAll();
    }
    public Mentor findById(String id) {
        return mentorRepository.findById(id).orElse(null);
    }

    public Mentor add(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    public Mentor update(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    public void delete(Mentor mentor) {
        mentorRepository.delete(mentor);
    }
}