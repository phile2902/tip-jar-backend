package com.bitcoin.interview.controller;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.service.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/managements")
public class ManagementController {
    private final IPaymentService paymentService;

    private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);
    
    @DeleteMapping("/payments/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") Long id)
    {
        paymentService.deleteById(id);
        return new ResponseEntity<>("Payment was deleted successful", HttpStatus.OK);
    }
    
    @PutMapping("/payments/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable(value = "id") Long id,
                                                 @Valid @RequestBody Payment payment) {
        logger.info("Update payment request: {}", payment);
        return new ResponseEntity<>(paymentService.updateById(id, payment), HttpStatus.OK);
    }
}
