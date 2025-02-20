package zafus.rubikbmt.rubikbmt_website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zafus.rubikbmt.rubikbmt_website.entities.LevelRating;
import zafus.rubikbmt.rubikbmt_website.repositories.ILevelRatingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LevelRatingService {
    @Autowired
    private ILevelRatingRepository levelRatingRepository;

    public List<LevelRating> getAllLevelRatings() {
        return levelRatingRepository.findAll();
    }

    public Optional<LevelRating> getLevelRatingById(String id) {
        return levelRatingRepository.findById(id);
    }

    public LevelRating saveLevelRating(LevelRating levelRating) {
        return levelRatingRepository.save(levelRating);
    }

    public void deleteLevelRating(String id) {
        levelRatingRepository.deleteById(id);
    }
}