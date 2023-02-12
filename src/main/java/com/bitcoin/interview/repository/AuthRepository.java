package com.bitcoin.interview.repository;

import com.bitcoin.interview.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByKey(String key);

    Optional<Auth> findByUserId(Long userId);
}
