package zafus.rubikbmt.rubikbmt_website.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zafus.rubikbmt.rubikbmt_website.entities.CubeSubject;
import zafus.rubikbmt.rubikbmt_website.repositories.ICubeSubjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CubeSubjectService {
    @Autowired
    private ICubeSubjectRepository cubeSubjectRepository;

    public List<CubeSubject> getAllCubeSubjects() {
        return cubeSubjectRepository.findAll();
    }

    public Optional<CubeSubject> getCubeSubjectById(String id) {
        return cubeSubjectRepository.findById(id);
    }

    public CubeSubject saveCubeSubject(CubeSubject cubeSubject) {
        return cubeSubjectRepository.save(cubeSubject);
    }

    public void deleteCubeSubject(String id) {
        cubeSubjectRepository.deleteById(id);
    }
}