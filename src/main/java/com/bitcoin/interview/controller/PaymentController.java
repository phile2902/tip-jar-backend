package com.bitcoin.interview.controller;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.service.IPaymentService;
import com.bitcoin.interview.service.exception.PaymentNotFoundException;
import com.bitcoin.interview.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1")
public class PaymentController {
    private final IPaymentService paymentService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    /**
     * This api to return all payments history belongs to a known user
     */
    @GetMapping("/user/{userId}/payments")
    public ResponseEntity<List<Payment>> getAll(@PathVariable(value = "userId") Long userId) throws UserNotFoundException {
        return ResponseEntity.ok().body(paymentService.getAllByUserId(userId));
    }

    /**
     * This api to return the latest history belongs to a known user
     */
    @GetMapping("/user/{userId}/payments/latest")
    public ResponseEntity<Payment> getLatestPayment(@PathVariable(value = "userId") Long userId) {
        return ResponseEntity.ok().body(paymentService.getLatestByUserId(userId));
    }

    /**
     * This api is to create payment for user
     */
    @PostMapping("/user/{userId}/payment")
    public ResponseEntity<Payment> createPayment(@PathVariable(value = "userId") Long userId,
                                                 @Valid @RequestBody Payment payment) {
        logger.info("Create payment request: {}", payment);
        return new ResponseEntity<>(paymentService.createByUserId(userId, payment), HttpStatus.CREATED);
    }
    
    /**
     * This api to return the total amount of payments during a period
     */
    @GetMapping("/user/{userId}/payments/calculateTotal")
    public ResponseEntity<Double> getTotalPaymentInPeriod(@PathVariable(value = "userId") Long userId, @RequestParam String from, @RequestParam String to) 
    throws PaymentNotFoundException {
        return ResponseEntity.ok().body(paymentService.getTotalByUserIdInPeriod(userId, from, to));
    }
    
    /**
     * This api to return the latest history belongs to a known user
     */
    @GetMapping("/user/{userId}/payments/mostExpensive")
    public ResponseEntity<Payment> getMostExpensivePayment(@PathVariable(value = "userId") Long userId) 
    throws PaymentNotFoundException {
        return ResponseEntity.ok().body(paymentService.getMostExpensiveByUserId(userId));
    }
}
