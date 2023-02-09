package com.bitcoin.interview.repository;

import com.bitcoin.interview.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsAdminTrue(Long id);
}
