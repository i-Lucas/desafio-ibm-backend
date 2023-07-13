package com.api.management.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.management.models.CandidateModel;

public interface CandidateRepository extends JpaRepository<CandidateModel, UUID> {

    CandidateModel findByEmail(String email);

    CandidateModel findByEmailAndUserModelId(String email, UUID userId);
}
