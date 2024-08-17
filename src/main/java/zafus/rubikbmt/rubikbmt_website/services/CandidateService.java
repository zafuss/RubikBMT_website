package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.repositories.ICandidateRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class CandidateService {
    private final ICandidateRepository candidateRepository;
    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    public Candidate findById(String id) {
        return candidateRepository.findById(id).orElse(null);
    }

    public Candidate add(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate update(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public void delete(Candidate candidate) {
        candidateRepository.delete(candidate);
    }
}
