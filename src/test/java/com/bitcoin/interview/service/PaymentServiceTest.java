package com.bitcoin.interview.service;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.model.User;
import com.bitcoin.interview.repository.PaymentRepository;
import com.bitcoin.interview.repository.UserRepository;
import com.bitcoin.interview.service.PaymentService;
import com.bitcoin.interview.service.exception.FailedToCreatePaymentException;
import com.bitcoin.interview.service.exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentServiceTest {
    @Mock
    private UserRepository userRepository = mock(UserRepository.class);

    @Mock
    private PaymentRepository paymentRepository = mock(PaymentRepository.class);

    @InjectMocks
    private PaymentService paymentService;
    
    @Test
    public void testGetAllByUserId() {
        Long userId = 1L;

        Payment payment01 = Mockito.mock(Payment.class);
        Payment payment02 = Mockito.mock(Payment.class);

        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment01);
        paymentList.add(payment02);

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(paymentRepository.findByUserId(userId)).thenReturn(paymentList);

        List<Payment> foundPayments = paymentService.getAllByUserId(userId);

        assertEquals(paymentList.size(), foundPayments.size());
        assertTrue(
            foundPayments.containsAll(paymentList) &&
            paymentList.containsAll(foundPayments)
        );
    }
    
    @Test
    public void testGetAllByUserId_UserNotFound() {
        // Given
        long userId = 1L;
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> paymentService.getAllByUserId(userId));
    }
    
    @Test
    public void testGetLatestByUserId() {
        Long userId = 1L;
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));

        Payment payment = Mockito.mock(Payment.class);

        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(paymentRepository.findByUserId(userId, Sort.by(orders))).thenReturn(paymentList);

        assertEquals(payment, paymentService.getLatestByUserId(userId));
    }
    
    @Test
    public void testGetLatestByUserIdThrowNotFoundUser() {
        Long userId = 1L;
        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getLatestByUserId(userId));
    }
    
    @Test
    public void testGetLatestByUserIdThrowNotFoundPayment() {
        Long userId = 1L;

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(paymentRepository.findByUserId(userId, Sort.by(orders))).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getLatestByUserId(userId));
    }
    
    @Test
    public void testCreateByUserId() {
        Long userId = 1L;
        User user = Mockito.mock(User.class);

        Payment newPayment = Mockito.mock(Payment.class);
        Payment savedPayment = Mockito.mock(Payment.class);

        Mockito.doNothing().when(newPayment).setUser(user);

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(paymentRepository.save(newPayment)).thenReturn(savedPayment);

        assertEquals(savedPayment, paymentService.createByUserId(userId, newPayment));
    }
    
    @Test
    public void testCreateByUserIdThrowFailedToSavePayment() {
        Long userId = 1L;
        User user = Mockito.mock(User.class);

        Payment newPayment = Mockito.mock(Payment.class);

        Mockito.doNothing().when(newPayment).setUser(user);
        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(paymentRepository.save(newPayment)).thenReturn(null);

        assertThrows(FailedToCreatePaymentException.class, () -> paymentService.createByUserId(userId, newPayment));
    }
    
    @Test
    public void testGetMostExpensiveByUserId() {
        Long userId = 1L;
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "amount"));

        Payment payment = Mockito.mock(Payment.class);

        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(paymentRepository.findByUserId(userId, Sort.by(orders))).thenReturn(paymentList);

        assertEquals(payment, paymentService.getMostExpensiveByUserId(userId));
    }
    
    @Test
    public void testGetMostExpensiveByUserIdThrowNotFoundUser() {
        Long userId = 1L;
        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getMostExpensiveByUserId(userId));
    }
    
    @Test
    public void testGetMostExpensiveByUserIdThrowNotFoundPayment() {
        Long userId = 1L;

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "amount"));

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(paymentRepository.findByUserId(userId, Sort.by(orders))).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getMostExpensiveByUserId(userId));
    }
    
    @Test
    public void testGetTotalByUserIdInPeriod()
    {
        Long userId = 1L;
        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        
        String from = "2023-01-01";
        String to = "2023-02-02";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromTime = LocalDate.parse(from, formatter);
        LocalDate toTime = LocalDate.parse(to, formatter);
        
        Payment payment01 = Mockito.mock(Payment.class);
        when(payment01.getAmount()).thenReturn(100.00);
        
        Payment payment02 = Mockito.mock(Payment.class);
        when(payment02.getAmount()).thenReturn(200.00);

        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment01);
        paymentList.add(payment02);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(paymentRepository.findByUserIdAndBetweenCreateDateTime(userId, fromTime, toTime)).thenReturn(paymentList);
        
        assertEquals(300, paymentService.getTotalByUserIdInPeriod(userId, from, to));
    }
    
    @Test
    public void testGetTotalByUserIdInPeriodNotFoundUser()
    {
        Long userId = 1L;
        String from = "2023-01-01";
        String to = "2023-02-02";
        
        when(userRepository.existsById(userId)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> paymentService.getTotalByUserIdInPeriod(userId, from, to));
    }
    
    @Test
    public void testGetTotalByUserIdInPeriodNotFoundPayment()
    {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        
        String from = "2023-01-01";
        String to = "2023-02-02";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromTime = LocalDate.parse(from, formatter);
        LocalDate toTime = LocalDate.parse(to, formatter);
        
        when(userRepository.existsById(userId)).thenReturn(true);
        when(paymentRepository.findByUserIdAndBetweenCreateDateTime(userId, fromTime, toTime)).thenReturn(Collections.emptyList());
        
        assertThrows(ResourceNotFoundException.class, () -> paymentService.getTotalByUserIdInPeriod(userId, from, to));
    }
    
    @Test
    public void testDeleteById()
    {
        Long id = 1L;
        
        Payment payment = Mockito.mock(Payment.class);
        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
        Mockito.doNothing().when(paymentRepository).deleteById(id);
    }
    
    @Test
    public void testDeleteByIdNotFoundPayment()
    {
        Long id = 1L;
        when(paymentRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
        
        assertThrows(ResourceNotFoundException.class, () -> paymentService.deleteById(id));
    }
    
    @Test
    public void testUpdateByUserId() {
        Long id = 1L;

        Payment newPayment = Mockito.mock(Payment.class);
        when(newPayment.getAmount()).thenReturn(100.00);
        when(newPayment.getTip()).thenReturn(5.00);
        when(newPayment.getThumbnail()).thenReturn("testThumbnail");
        
        Payment existingPayment = Mockito.mock(Payment.class);
        Mockito.doNothing().when(existingPayment).setAmount(100.00);
        Mockito.doNothing().when(existingPayment).setTip(5.00);
        Mockito.doNothing().when(existingPayment).setThumbnail("testThumbnail");

        Mockito.when(paymentRepository.findById(id)).thenReturn(Optional.of(existingPayment));
        Mockito.when(paymentRepository.save(existingPayment)).thenReturn(existingPayment);

        assertEquals(existingPayment, paymentService.updateById(id, newPayment));
    }
    
    @Test
    public void testUpdateByUserIdNotFoundPayment() {
        Long id = 1L;
        Payment newPayment = Mockito.mock(Payment.class);

        Mockito.when(paymentRepository.findById(id)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> paymentService.updateById(id, newPayment));
    }
}
