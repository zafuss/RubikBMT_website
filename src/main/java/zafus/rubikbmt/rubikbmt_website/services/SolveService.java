package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Event;
import zafus.rubikbmt.rubikbmt_website.entities.Solve;
import zafus.rubikbmt.rubikbmt_website.repositories.IEventRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.ISolveRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class SolveService {
    private final ISolveRepository solveRepository;

    public List<Solve> findAll() {
        return solveRepository.findAll();
    }

    public Solve findById(String id) {
        return solveRepository.findById(id).orElse(null);
    }

    public Solve add(Solve event) {
        return solveRepository.save(event);
    }

    public Solve update(Solve event) {
        return solveRepository.save(event);
    }

    public void delete(Solve event) {
        solveRepository.delete(event);
    }
}
