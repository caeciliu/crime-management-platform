package com.crime.management.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "crime_events")
public class CrimeEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "案件标题不能为空")
    @Size(min = 5, max = 200, message = "案件标题长度必须在5-200个字符之间")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "案件类型不能为空")
    @Column(nullable = false)
    private String crimeType;

    @NotBlank(message = "案发地点不能为空")
    @Column(nullable = false)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "案发时间不能为空")
    @Column(name = "incident_time", nullable = false)
    private LocalDateTime incidentTime;

    @Column(name = "report_time")
    private LocalDateTime reportTime;

    @Column(name = "status")
    private String status = "待处理";

    @Column(name = "severity_level")
    private String severityLevel;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (reportTime == null) {
            reportTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 构造函数
    public CrimeEvent() {}

    public CrimeEvent(String title, String crimeType, String location, String description,
                     LocalDateTime incidentTime, String severityLevel) {
        this.title = title;
        this.crimeType = crimeType;
        this.location = location;
        this.description = description;
        this.incidentTime = incidentTime;
        this.severityLevel = severityLevel;
        this.reportTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getIncidentTime() {
        return incidentTime;
    }

    public void setIncidentTime(LocalDateTime incidentTime) {
        this.incidentTime = incidentTime;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}