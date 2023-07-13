package com.api.management.services;

import com.api.management.dtos.CandidateDto;
import com.api.management.exceptions.user.UserNotFoundException;
import com.api.management.models.CandidateModel;
import com.api.management.models.UserModel;
import com.api.management.repositories.CandidateRepository;
import com.api.management.types.StatusCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

@Service
public class CandidateService {

    @Autowired
    private UserService userService;

    @Autowired
    private CandidateRepository candidateRepository;

    public UUID addCandidate(String token, CandidateDto request) {
        var user = userService.getUserByToken(token);

        var existingCandidate = candidateRepository.findByEmailAndUserModelId(request.getEmail(), user.getId());
        if (existingCandidate != null) {
            System.out.println(existingCandidate.getUserModel().getEmail());
            throw new IllegalArgumentException("Candidate email already exists.");
        }

        var candidate = new CandidateModel();
        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());
        candidate.setStatus(StatusCandidate.Received);
        candidate.setUserModel(user);

        user.getCandidates().add(candidate);
        var savedCandidate = candidateRepository.save(candidate);
        return savedCandidate.getId();
    }

    public UUID scheduleCandidate(String token, @Valid Map<String, UUID> requestBody) {
        var candidate = getCandidateById(requestBody.get("candidateId"));
        var user = userService.getUserByToken(token);

        validateCandidateOwnership(candidate, user);

        candidate.setStatus(StatusCandidate.Qualified);
        var candidateId = candidate.getId();
        candidateRepository.save(candidate);
        return candidateId;
    }

    public UUID disqualifyCandidate(String token, Map<String, UUID> requestBody) {
        var user = userService.getUserByToken(token);

        if (!requestBody.containsKey("candidateId")) {
            throw new IllegalArgumentException("Candidate ID not provided.");
        }

        UUID candidateId = requestBody.get("candidateId");
        var candidate = getCandidateById(candidateId);

        validateCandidateOwnership(candidate, user);

        candidate.setStatus(StatusCandidate.Disqualified);
        candidateRepository.save(candidate);

        return candidate.getId();
    }

    public UUID approveCandidate(String token, Map<String, UUID> requestBody) {
        var user = userService.getUserByToken(token);

        var candidate = getCandidateById(requestBody.get("candidateId"));
        validateCandidateOwnership(candidate, user);

        candidate.setStatus(StatusCandidate.Approved);
        candidateRepository.save(candidate);
        return candidate.getId();
    }

    public Map<String, Object> getCandidateStatus(String token, UUID candidateId) {
        var user = userService.getUserByToken(token);
        var candidate = getCandidateById(candidateId);
        validateCandidateOwnership(candidate, user);

        var response = new HashMap<String, Object>();
        response.put("nome", candidate.getName());
        response.put("email", candidate.getEmail());
        response.put("status", candidate.getStatus());

        return response;
    }

    public List<Map<String, Object>> getApprovedCandidates(String token) {
        var user = userService.getUserByToken(token);
        var candidates = user.getCandidates();

        return candidates.stream()
                .filter(candidate -> candidate.getStatus() == StatusCandidate.Approved)
                .map(candidate -> {
                    var candidateInfo = new HashMap<String, Object>();
                    candidateInfo.put("nome", candidate.getName());
                    candidateInfo.put("email", candidate.getEmail());
                    candidateInfo.put("status", candidate.getStatus());
                    return candidateInfo;
                })
                .collect(Collectors.toList());
    }

    private CandidateModel getCandidateById(UUID candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(UserNotFoundException::new);
    }

    private void validateCandidateOwnership(CandidateModel candidate, UserModel user) {
        if (!candidate.getUserModel().equals(user)) {
            throw new IllegalArgumentException("Unauthorized");
        }
    }
}
