package com.bitcoin.interview.service;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.repository.PaymentRepository;
import com.bitcoin.interview.repository.UserRepository;
import com.bitcoin.interview.service.exception.FailedToCreatePaymentException;
import com.bitcoin.interview.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAllByUserId(Long userId) {
        checkUserExist(userId);
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public Payment createByUserId(Long userId, Payment payment) {
        checkUserExist(userId);
        Optional<Payment> savedPayment = userRepository.findById(userId).map(
                user -> {
                    payment.setUser(user);
                    return paymentRepository.save(payment);
                }
        );

        //If saving payment is not successful
        if (savedPayment.isEmpty()) {
            throw new FailedToCreatePaymentException("Payment is failed to be created");
        }

        return savedPayment.get();
    }

    @Override
    public Payment getLatestByUserId(Long userId) {
        return getPaymentByUserIdWithMaxValueOfProperty(userId, "createdAt");
    }

    private void checkUserExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not found user: " + userId);
        }
    }

    @Override
    public Double getTotalByUserIdInPeriod(Long userId, String from, String to) {
        checkUserExist(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromTime = LocalDate.parse(from, formatter);
        LocalDate toTime = LocalDate.parse(to, formatter);

        List<Payment> foundPayments = paymentRepository.findByUserIdAndBetweenCreateDateTime(userId, fromTime, toTime);

        //If user has no payments
        if (foundPayments.isEmpty()) {
            throw new ResourceNotFoundException("Not found payments");
        }

        Double total = 0.0;
        for (Payment payment : foundPayments) {
            total += payment.getAmount();
        }

        return total;
    }

    @Override
    public Payment getMostExpensiveByUserId(Long userId) {
        return getPaymentByUserIdWithMaxValueOfProperty(userId, "amount");
    }

    private Payment getPaymentByUserIdWithMaxValueOfProperty(Long userId, String property) {
        checkUserExist(userId);
        //Create order
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, property));
        //Find list of sorted payments
        List<Payment> sortedPayments = paymentRepository.findByUserId(userId, Sort.by(orders));
        //If user has no payments
        if (sortedPayments.isEmpty()) {
            throw new ResourceNotFoundException("Not found payment");
        }

        return sortedPayments.get(0);
    }

    @Override
    public void deleteById(Long id) {
        //Check if payment exists
        paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found payment"));

        paymentRepository.deleteById(id);
    }

    @Override
    public Payment updateById(Long id, Payment payment) {
        //Found existing payment and update with new data, we dont allow to update user relation to the payment
        Payment existingPayment = paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found payment"));
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setTip(payment.getTip());
        existingPayment.setThumbnail(payment.getThumbnail());

        return paymentRepository.save(existingPayment);
    }
}
