package com.nhnacademy.accountapi.repository;

import com.nhnacademy.accountapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
