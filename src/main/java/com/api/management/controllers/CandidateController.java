package com.api.management.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.management.dtos.CandidateDto;
import com.api.management.services.CandidateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/hiring/")
@CrossOrigin(origins = "*")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping("/start")
    public ResponseEntity<Object> addCandidate(@RequestHeader("Authorization") String token,
            @RequestBody CandidateDto request) {

        var candidateId = candidateService.addCandidate(token, request);

        var response = new HashMap<>();
        response.put("message", "Candidate added successfully.");
        response.put("status", HttpStatus.CREATED);
        response.put("candidateId", candidateId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/schedule")
    public ResponseEntity<Object> scheduleCandidate(@RequestHeader("Authorization") String token,
            @Valid @RequestBody Map<String, UUID> requestBody) {

        var candidateId = candidateService.scheduleCandidate(token, requestBody);

        var response = new HashMap<>();
        response.put("message", "Candidate scheduled successfully.");
        response.put("candidateId", candidateId);
        response.put("status", HttpStatus.OK);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/disqualify")
    public ResponseEntity<Object> disqualifyCandidate(@RequestHeader("Authorization") String token,
            @RequestBody Map<String, UUID> requestBody) {
                
       var candidateId = candidateService.disqualifyCandidate(token, requestBody);

        var response = new HashMap<>();
        response.put("message", "Candidate disqualified successfully.");
        response.put("candidateId", candidateId);
        response.put("status", HttpStatus.OK);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/approve")
    public ResponseEntity<Object> approveCandidate(@RequestHeader("Authorization") String token,
            @RequestBody Map<String, UUID> requestBody) {

        var candidateId = candidateService.approveCandidate(token, requestBody);

        var response = new HashMap<>();
        response.put("message", "Candidate approved successfully.");
        response.put("candidateId", candidateId);
        response.put("status", HttpStatus.OK);

        return ResponseEntity.ok(response);
    }

    @GetMapping("status/candidate/{candidateId}")
    public ResponseEntity<Object> getCandidateStatus(@RequestHeader("Authorization") String token,
            @PathVariable("candidateId") UUID candidateId) {
        return ResponseEntity.ok(candidateService.getCandidateStatus(token, candidateId));
    }

    @GetMapping("/approved")
    public ResponseEntity<Object> getApprovedCandidates(@RequestHeader("Authorization") String token) {
        List<Map<String, Object>> approvedCandidates = candidateService.getApprovedCandidates(token);
        return ResponseEntity.ok(approvedCandidates);
    }
}
