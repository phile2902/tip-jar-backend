package com.bitcoin.interview.service;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.service.exception.UserNotFoundException;

import java.util.List;

public interface IPaymentService {
    List<Payment> getAllByUserId(Long userId) throws UserNotFoundException;

    Payment createByUserId(Long userId, Payment payment);

    Payment getLatestByUserId(Long userId);
    
    Double getTotalByUserIdInPeriod(Long userId, String from, String to);
    
    Payment getMostExpensiveByUserId(Long userId);
    
    void deleteById(Long id);
    
    Payment updateById(Long id, Payment payment);
}
