package com.crime.management.backend.controller;

import com.crime.management.backend.model.CrimeEvent;
import com.crime.management.backend.service.CrimeEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crime-events")
@CrossOrigin(origins = {"http://localhost:80", "http://localhost:3000", "http://127.0.0.1:80", "http://127.0.0.1:3000"}, maxAge = 3600, allowCredentials = "true")
public class CrimeEventController {

    @Autowired
    private CrimeEventService crimeEventService;

    @GetMapping
    public ResponseEntity<List<CrimeEvent>> getAllCrimeEvents() {
        List<CrimeEvent> events = crimeEventService.getAllCrimeEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CrimeEvent>> getCrimeEventsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CrimeEvent> events = crimeEventService.getCrimeEventsPage(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrimeEvent> getCrimeEventById(@PathVariable Long id) {
        CrimeEvent event = crimeEventService.getCrimeEventById(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<CrimeEvent> createCrimeEvent(@Valid @RequestBody CrimeEvent crimeEvent) {
        CrimeEvent createdEvent = crimeEventService.createCrimeEvent(crimeEvent);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CrimeEvent> updateCrimeEvent(
            @PathVariable Long id,
            @Valid @RequestBody CrimeEvent eventDetails) {
        CrimeEvent updatedEvent = crimeEventService.updateCrimeEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CrimeEvent> updateCrimeEventStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusMap) {
        String status = statusMap.get("status");
        CrimeEvent updatedEvent = crimeEventService.updateCrimeEventStatus(id, status);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCrimeEvent(@PathVariable Long id) {
        crimeEventService.deleteCrimeEvent(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CrimeEvent>> searchCrimeEvents(
            @RequestParam(required = false) String crimeType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severityLevel,
            @RequestParam(required = false) String location) {
        List<CrimeEvent> events = crimeEventService.searchCrimeEvents(crimeType, status, severityLevel, location);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search/time-range")
    public ResponseEntity<List<CrimeEvent>> searchByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<CrimeEvent> events = crimeEventService.searchByTimeRange(startTime, endTime);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getCrimeStatistics() {
        Map<String, Long> statistics = crimeEventService.getCrimeStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "犯罪事件管理服务运行正常");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection() {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = crimeEventService.countAllEvents();
            response.put("status", "SUCCESS");
            response.put("message", "数据库连接正常");
            response.put("totalEvents", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "数据库连接失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}