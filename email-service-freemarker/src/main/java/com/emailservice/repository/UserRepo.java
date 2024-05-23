package com.emailservice.repository;

import com.emailservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Long> {
    List<User> findByRole(String role);
}
