package com.example.appliancemgmt.service;

import com.example.appliancemgmt.dto.LogResponseDTO;
import com.example.appliancemgmt.entity.Log;
import com.example.appliancemgmt.entity.User;
import com.example.appliancemgmt.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserService userService;

    public void logAction(String action, String entity, String details) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalStateException("User not found for username: " + username);
        }
        Log log = new Log();
        log.setTimestamp(LocalDateTime.now());
        log.setAction(action);
        log.setEntity(entity);
        log.setDetails(details);
        log.setUser(user);
        log.setCreatedAt(LocalDateTime.now());
        logRepository.save(log);
    }

    public Page<LogResponseDTO> getLogs(int page, int size, String sortField, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Log> logPage = logRepository.findAll(pageRequest);

        List<LogResponseDTO> logDTOs = logPage.getContent().stream()
                .map(log -> new LogResponseDTO(
                        log.getId(),
                        log.getTimestamp(),
                        log.getAction(),
                        log.getEntity(),
                        log.getDetails(),
                        new LogResponseDTO.UserDTO(log.getUser().getUsername()),
                        log.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(logDTOs, pageRequest, logPage.getTotalElements());
    }
}