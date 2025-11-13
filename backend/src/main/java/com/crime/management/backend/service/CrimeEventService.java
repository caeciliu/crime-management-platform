package com.crime.management.backend.service;

import com.crime.management.backend.model.CrimeEvent;
import com.crime.management.backend.repository.CrimeEventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CrimeEventService {

    @Autowired
    private CrimeEventRepository crimeEventRepository;

    public List<CrimeEvent> getAllCrimeEvents() {
        return crimeEventRepository.findAll();
    }

    public Page<CrimeEvent> getCrimeEventsPage(Pageable pageable) {
        return crimeEventRepository.findAll(pageable);
    }

    public CrimeEvent getCrimeEventById(Long id) {
        return crimeEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("犯罪事件不存在，ID: " + id));
    }

    public CrimeEvent createCrimeEvent(CrimeEvent crimeEvent) {
        return crimeEventRepository.save(crimeEvent);
    }

    public CrimeEvent updateCrimeEvent(Long id, CrimeEvent eventDetails) {
        CrimeEvent existingEvent = getCrimeEventById(id);

        existingEvent.setTitle(eventDetails.getTitle());
        existingEvent.setCrimeType(eventDetails.getCrimeType());
        existingEvent.setLocation(eventDetails.getLocation());
        existingEvent.setDescription(eventDetails.getDescription());
        existingEvent.setIncidentTime(eventDetails.getIncidentTime());
        existingEvent.setStatus(eventDetails.getStatus());
        existingEvent.setSeverityLevel(eventDetails.getSeverityLevel());

        return crimeEventRepository.save(existingEvent);
    }

    public void deleteCrimeEvent(Long id) {
        if (!crimeEventRepository.existsById(id)) {
            throw new EntityNotFoundException("犯罪事件不存在，ID: " + id);
        }
        crimeEventRepository.deleteById(id);
    }

    // 按条件搜索犯罪事件
    public List<CrimeEvent> searchCrimeEvents(String crimeType, String status, String severityLevel, String location) {
        return crimeEventRepository.findByMultipleConditions(crimeType, status, severityLevel, location);
    }

    // 按时间范围搜索
    public List<CrimeEvent> searchByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return crimeEventRepository.findByIncidentTimeBetween(startTime, endTime);
    }

    // 更新犯罪事件状态
    public CrimeEvent updateCrimeEventStatus(Long id, String status) {
        CrimeEvent existingEvent = getCrimeEventById(id);
        existingEvent.setStatus(status);
        return crimeEventRepository.save(existingEvent);
    }

    // 获取犯罪统计数据
    public Map<String, Long> getCrimeStatistics() {
        List<Object[]> crimeTypeStats = crimeEventRepository.countByCrimeType();
        Map<String, Long> statistics = crimeTypeStats.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));

        List<Object[]> statusStats = crimeEventRepository.countByStatus();
        Map<String, Long> statusStatistics = statusStats.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));

        statistics.putAll(statusStatistics);
        return statistics;
    }
}