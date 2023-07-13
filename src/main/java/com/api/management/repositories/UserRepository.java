package com.api.management.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.management.models.CandidateModel;
import com.api.management.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByEmail(String email);

    @Query("SELECT c FROM CandidateModel c WHERE c.userModel = ?1")
    List<CandidateModel> findAllCandidatesByUser(UserModel user);
}
