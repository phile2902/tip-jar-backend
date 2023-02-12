package com.bitcoin.interview.repository;

import com.bitcoin.interview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsAdminTrue(Long id);
}
