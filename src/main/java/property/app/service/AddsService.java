package property.app.service;


import property.app.beans.Adds;
import property.app.repository.AddRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddsService {

    private final AddRepository addRepository;

    public AddsService(AddRepository addRepository) {
        this.addRepository = addRepository;
    }

    public List<Adds> findAll() {
        return addRepository.findAll();
    }

    public Optional<Adds> findById(Long id) {
        return addRepository.findById(id);
    }

    public void deleteById(Long id) {
        addRepository.deleteById(id);
    }


    public List<Adds> findByUserId(Long id) {
        return addRepository.findByUserId(id);
    }

    public List<Adds> search(String search) {
        return addRepository.findByTitleLikeOrUrlLike(search, search);
    }

    public Adds save(Adds adds) {
        return addRepository.save(adds);
    }

}
