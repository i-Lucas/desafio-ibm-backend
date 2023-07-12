package com.api.management.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.management.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByEmail(String email);
}
