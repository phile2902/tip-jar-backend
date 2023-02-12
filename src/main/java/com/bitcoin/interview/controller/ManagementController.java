package com.bitcoin.interview.controller;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.service.IPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/managements")
public class ManagementController {
    private final IPaymentService paymentService;

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") Long id) {
        paymentService.deleteById(id);
        return new ResponseEntity<>("Payment was deleted successful", HttpStatus.OK);
    }

    @PutMapping("/payments/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable(value = "id") Long id,
                                                 @Valid @RequestBody Payment payment) {
        log.info("Update payment request: {}", payment);
        return new ResponseEntity<>(paymentService.updateById(id, payment), HttpStatus.OK);
    }
}
