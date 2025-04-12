package com.example.appliancemgmt.repository;

import com.example.appliancemgmt.entity.Session;
import com.example.appliancemgmt.entity.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByApplianceId(Long applianceId);
    List<Session> findByStatus(SessionStatus status);
}