package com.api.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.management.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
}
