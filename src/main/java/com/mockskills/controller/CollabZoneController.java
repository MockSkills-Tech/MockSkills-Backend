package com.mockskills.controller;

import com.mockskills.model.CollabZone;
import com.mockskills.service.CollabZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collabzone")
public class CollabZoneController {

    private final CollabZoneService collabZoneService;

    @Autowired
    public CollabZoneController(CollabZoneService collabZoneService) {
        this.collabZoneService = collabZoneService;
    }

    // Fetch all registrations
    @GetMapping("/registrations")
    public ResponseEntity<List<CollabZone>> getAllRegistrations() {
        List<CollabZone> registrations = collabZoneService.getAllRegistrations();
        return ResponseEntity.ok(registrations);  // Return all registrations with 200 OK
    }

    // Fetch a single registration by ID
    @GetMapping("/registration/{id}")
    public ResponseEntity<Object> getRegistrationById(@PathVariable Long id) {
        return collabZoneService.getRegistrationById(id);
    }

    // Create a new registration
    @PostMapping("/registration")
    public ResponseEntity<String> createRegistration(@RequestBody CollabZone collabZone) {
        return collabZoneService.createRegistration(collabZone);
    }
}
