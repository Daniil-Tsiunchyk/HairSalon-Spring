package bsuir.coursework.HairSalon.services;

import bsuir.coursework.HairSalon.models.HairService;
import bsuir.coursework.HairSalon.repositories.HairServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HairServiceService {
    private final HairServiceRepository hairServiceRepository;

    @Autowired
    public HairServiceService(HairServiceRepository hairServiceRepository) {
        this.hairServiceRepository = hairServiceRepository;
    }

    public List<HairService> getAllHairServices() {
        return hairServiceRepository.findAll();
    }

    public Optional<HairService> getHairServiceById(int id) {
        return hairServiceRepository.findById(id);
    }

    public HairService createHairService(HairService hairService) {
        return hairServiceRepository.save(hairService);
    }

    public HairService updateHairService(int id, HairService updatedHairService) {
        Optional<HairService> existingHairService = hairServiceRepository.findById(id);
        if (existingHairService.isPresent()) {
            HairService hairService = existingHairService.get();
            hairService.setName(updatedHairService.getName());
            hairService.setCost(updatedHairService.getCost());
            return hairServiceRepository.save(hairService);
        } else {
            return null;
        }
    }

    public void deleteHairService(int id) {
        hairServiceRepository.deleteById(id);
    }
}
