package com.bitcoin.interview.service;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.model.User;
import com.bitcoin.interview.repository.PaymentRepository;
import com.bitcoin.interview.repository.UserRepository;
import com.bitcoin.interview.service.PaymentServiceImpl;
import com.bitcoin.interview.service.exception.FailedToCreatePaymentException;
import com.bitcoin.interview.service.exception.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceImplTest {
    @Mock
    private UserRepository userRepository = mock(UserRepository.class);

    @Mock
    private PaymentRepository paymentRepository = mock(PaymentRepository.class);

    @InjectMocks
    private PaymentServiceImpl paymentService;

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

        Assert.assertEquals(paymentList.size(), foundPayments.size());
        Assert.assertTrue(
    foundPayments.containsAll(paymentList) &&
            paymentList.containsAll(foundPayments)
        );
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetAllByUserIdThrowsOnUserNotFound() {
        Long userId = 1L;
        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        paymentService.getAllByUserId(userId);
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

        Assert.assertEquals(payment, paymentService.getLatestByUserId(userId));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetLatestByUserIdThrowNotFoundUser() {
        Long userId = 1L;
        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        paymentService.getLatestByUserId(userId);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetLatestByUserIdThrowNotFoundPayment() {
        Long userId = 1L;

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));

        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(paymentRepository.findByUserId(userId, Sort.by(orders))).thenReturn(Collections.emptyList());

        paymentService.getLatestByUserId(userId);
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

        Assert.assertEquals(savedPayment, paymentService.createByUserId(userId, newPayment));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCreateByUserIdThrowNotFoundUser() {
        Long userId = 1L;
        Mockito.when(userRepository.existsById(userId)).thenReturn(false);

        paymentService.createByUserId(userId, Mockito.mock(Payment.class));
    }

    @Test(expected = FailedToCreatePaymentException.class)
    public void testCreateByUserIdThrowFailedToSavePayment() {
        Long userId = 1L;
        User user = Mockito.mock(User.class);

        Payment newPayment = Mockito.mock(Payment.class);

        Mockito.doNothing().when(newPayment).setUser(user);
        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(paymentRepository.save(newPayment)).thenReturn(null);

        paymentService.createByUserId(userId, newPayment);
    }
}
