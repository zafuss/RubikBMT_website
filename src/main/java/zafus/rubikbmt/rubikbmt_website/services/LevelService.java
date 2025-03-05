package zafus.rubikbmt.rubikbmt_website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zafus.rubikbmt.rubikbmt_website.entities.Level;
import zafus.rubikbmt.rubikbmt_website.repositories.ILevelRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LevelService {
    @Autowired
    private ILevelRepository levelRepository;

    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    public Optional<Level> getLevelById(String id) {
        return levelRepository.findById(id);
    }

    public Level createLevel(Level level) {
        return levelRepository.save(level);
    }

    public Level updateLevel(String id, Level updatedLevel) {
        if (levelRepository.existsById(id)) {
            updatedLevel.setId(id);
            return levelRepository.save(updatedLevel);
        }
        return null;
    }

    public void deleteLevel(String id) {
        levelRepository.deleteById(id);
    }
}