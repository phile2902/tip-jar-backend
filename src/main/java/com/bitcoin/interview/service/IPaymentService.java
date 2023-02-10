package com.bitcoin.interview.service;

import com.bitcoin.interview.model.Payment;

import java.util.List;

public interface IPaymentService {
    List<Payment> getAllByUserId(Long userId);

    Payment createByUserId(Long userId, Payment payment);

    Payment getLatestByUserId(Long userId);
    
    Double getTotalByUserIdInPeriod(Long userId, String from, String to);
    
    Payment getMostExpensiveByUserId(Long userId);
    
    void deleteById(Long id);
    
    Payment updateById(Long id, Payment payment);
}
