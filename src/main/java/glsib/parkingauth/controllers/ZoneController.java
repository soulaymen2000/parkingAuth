package glsib.parkingauth.controllers;

import glsib.parkingauth.services.ZoneService;
import glsib.parkingauth.entities.Zone;
import glsib.parkingauth.repositories.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/zones")
@CrossOrigin("*")  // Allow cross-origin requests
public class ZoneController {

    @Autowired
    private ZoneService zoneService;
    private ZoneRepository zoneRepository;

    public ZoneController(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @GetMapping
    public List<Zone> getAllZones() {
        return zoneService.getAllZones();
    }

    @GetMapping("/{id}")
    public Zone getZoneById(@PathVariable Long id) {
        return zoneService.getZoneById(id);
    }

    @PostMapping
    public Zone createZone(@RequestBody Zone zone) {
        return zoneService.saveZone(zone);
    }

    @DeleteMapping("/{id}")
    public void deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
    }
    @PostMapping("/reserve")
    public ResponseEntity<Zone> reserveSpot(@RequestBody Map<String, String> request) {
        String description = request.get("description");

        Zone zone = zoneRepository.findByDescription(description);
        if (zone == null || zone.getAvailableSpots() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        zone.setAvailableSpots(zone.getAvailableSpots() - 1);
        zoneRepository.save(zone);

        return ResponseEntity.ok(zone);
    }
}
