package com.api.management.models;

import java.util.UUID;

import com.api.management.types.StatusCandidate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "candidates")
public class CandidateModel {

    public CandidateModel(String name, String email, StatusCandidate status, UserModel userModel) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.userModel = userModel;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;
    private String name;
    private StatusCandidate status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel userModel;
}
