package com.bitcoin.interview.repository;

import com.bitcoin.interview.model.Payment;
import java.time.LocalDate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);

    List<Payment> findByUserId(Long userId, Sort sort);
    
    @Query(value = "select * from Payment p where p.created_at <= :toTime and p.created_at >= :fromTime and p.user_id = :userId", nativeQuery = true)
    List<Payment> findByUserIdAndBetweenCreateDateTime(@Param("userId") Long userId, @Param("fromTime") LocalDate fromTime, @Param("toTime") LocalDate toTime);
}
