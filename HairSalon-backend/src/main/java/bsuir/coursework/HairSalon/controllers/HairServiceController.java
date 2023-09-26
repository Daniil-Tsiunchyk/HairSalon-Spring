package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.HairService;
import bsuir.coursework.HairSalon.services.HairServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hair-services")
public class HairServiceController {
    private final HairServiceService hairServiceService;

    @Autowired
    public HairServiceController(HairServiceService hairServiceService) {
        this.hairServiceService = hairServiceService;
    }

    @GetMapping
    public ResponseEntity<List<HairService>> getAllHairServices() {
        List<HairService> hairServices = hairServiceService.getAllHairServices();
        return ResponseEntity.ok(hairServices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HairService> getHairServiceById(@PathVariable int id) {
        Optional<HairService> hairService = hairServiceService.getHairServiceById(id);
        return hairService.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HairService> createHairService(@RequestBody HairService hairService) {
        HairService createdHairService = hairServiceService.createHairService(hairService);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHairService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HairService> updateHairService(@PathVariable int id, @RequestBody HairService updatedHairService) {
        HairService updated = hairServiceService.updateHairService(id, updatedHairService);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHairService(@PathVariable int id) {
        hairServiceService.deleteHairService(id);
        return ResponseEntity.noContent().build();
    }
}
