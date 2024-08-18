package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.LearningType;
import zafus.rubikbmt.rubikbmt_website.repositories.ILearningTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class LearningTypeService {
    private final ILearningTypeRepository learningTypeRepository;

    public List<LearningType> findAll() {
        return learningTypeRepository.findAll();
    }

    public LearningType findById(String id) {
        return learningTypeRepository.findById(id).orElse(null);
    }

    public LearningType add(LearningType learningType) {
        return learningTypeRepository.save(learningType);
    }

    public LearningType update(LearningType learningType) {
        return learningTypeRepository.save(learningType);
    }

    public void delete(LearningType learningType) {
        learningTypeRepository.delete(learningType);
    }


}