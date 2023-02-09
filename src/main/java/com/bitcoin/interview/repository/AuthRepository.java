package com.bitcoin.interview.repository;

import com.bitcoin.interview.model.Auth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long>{
    Optional<Auth> findByKey(String key);
}
