package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.repositories.ICompetitionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class CompetitionService {
    private final ICompetitionRepository competitionRepo;

    public List<Competition> getAll() {
        return competitionRepo.findAll();
    }

    public Competition getById(String id) {
        return competitionRepo.findById(id).orElse(null);
    }

    public Competition getByName(String name) {
        return competitionRepo.findByName(name);
    }
    public Competition create(Competition competition) {
        return competitionRepo.save(competition);
    }

    public Competition update(Competition competition) {
        return competitionRepo.save(competition);
    }

    public void delete(Competition competition) {
        competitionRepo.delete(competition);
    }

}
