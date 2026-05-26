package com.nhnacademy.accountapi.repository;

import com.nhnacademy.accountapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdAndPassword(String id, String password);
}
