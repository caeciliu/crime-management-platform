package com.crime.management.backend.repository;

import com.crime.management.backend.model.CrimeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CrimeEventRepository extends JpaRepository<CrimeEvent, Long> {

    // 根据犯罪类型查找
    List<CrimeEvent> findByCrimeType(String crimeType);

    // 根据状态查找
    List<CrimeEvent> findByStatus(String status);

    // 根据严重程度查找
    List<CrimeEvent> findBySeverityLevel(String severityLevel);

    // 根据地点模糊查找
    List<CrimeEvent> findByLocationContainingIgnoreCase(String location);

    // 根据时间范围查找
    @Query("SELECT c FROM CrimeEvent c WHERE c.incidentTime BETWEEN :startTime AND :endTime")
    List<CrimeEvent> findByIncidentTimeBetween(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    // 多条件查询
    @Query("SELECT c FROM CrimeEvent c WHERE " +
           "(:crimeType IS NULL OR c.crimeType = :crimeType) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:severityLevel IS NULL OR c.severityLevel = :severityLevel) AND " +
           "(:location IS NULL OR LOWER(c.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<CrimeEvent> findByMultipleConditions(@Param("crimeType") String crimeType,
                                             @Param("status") String status,
                                             @Param("severityLevel") String severityLevel,
                                             @Param("location") String location);

    // 统计各类型犯罪数量
    @Query("SELECT c.crimeType, COUNT(c) FROM CrimeEvent c GROUP BY c.crimeType")
    List<Object[]> countByCrimeType();

    // 统计各状态犯罪数量
    @Query("SELECT c.status, COUNT(c) FROM CrimeEvent c GROUP BY c.status")
    List<Object[]> countByStatus();
}