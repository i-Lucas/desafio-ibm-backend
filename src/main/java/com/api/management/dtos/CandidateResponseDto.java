package com.api.management.dtos;

import java.util.UUID;

import com.api.management.types.StatusCandidate;

import lombok.Data;

@Data
public class CandidateResponseDto {

    private UUID id;
    private String name;
    private String email;
    private StatusCandidate status;
}
