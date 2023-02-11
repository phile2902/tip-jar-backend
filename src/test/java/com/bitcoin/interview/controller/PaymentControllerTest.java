package com.bitcoin.interview.controller;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.model.User;
import com.bitcoin.interview.service.IPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPaymentService paymentService;

    @Test
    public void testGetLatestPayment() throws Exception {
        User user = new User();
        Long userId = 1L;
        Payment payment = new Payment(2400.0, 240.0, "abc", user);

        when(paymentService.getLatestByUserId(userId)).thenReturn(payment);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/user/%s/payments/latest", userId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(2400.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip").value(240.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail").value("abc"))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testGetAll() throws Exception {
        User user = new User();
        Long userId = 1L;
        Payment payment01 = new Payment(2400.0, 240.0, "abc", user);
        Payment payment02 = new Payment(2300.0, 230.0, "abc", user);
        List<Payment> list = new ArrayList<>();
        list.add(payment01);
        list.add(payment02);

        when(paymentService.getAllByUserId(userId)).thenReturn(list);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/user/%s/payments", userId)))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<Payment> payments = Arrays.asList(mapper.readValue(result.getResponse().getContentAsString(), Payment[].class));
        assertEquals(payments.size(), 2);
    }
    
    @Test
    public void testCreatePayment() throws Exception {
        User user = new User();
        Long userId = 1L;
        Payment payment = new Payment(2400.0, 240.0, "abc", user);

        when(paymentService.createByUserId(eq(userId), any(Payment.class))).thenReturn(payment);

        mockMvc.perform(
        MockMvcRequestBuilders.post(String.format("/api/v1/user/%s/payment", userId))
                        .content(new ObjectMapper().writeValueAsString(payment))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(2400.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip").value(240.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail").value("abc"))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void testGetTotalPaymentInPeriod() throws Exception {
        Long userId = 1L;
        String from = "2023-01-01";
        String to = "2023-02-01";

        when(paymentService.getTotalByUserIdInPeriod(userId, from, to)).thenReturn(2000.0);

        MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.get(String.format("/api/v1/user/%s/payments/calculateTotal", userId))
                    .param("from", from)
                    .param("to", to)
                )
                .andExpect(status().isOk())
                .andReturn();
        
        assertEquals("2000.0", result.getResponse().getContentAsString());
    }
    
    @Test
    public void testMostExpensivePayment() throws Exception {
        Long userId = 1L;
        User user = new User();
        Payment payment = new Payment(2400.0, 240.0, "abc", user);

        when(paymentService.getMostExpensiveByUserId(userId)).thenReturn(payment);

        mockMvc.perform(
        MockMvcRequestBuilders.get(String.format("/api/v1/user/%s/payments/mostExpensive", userId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(2400.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip").value(240.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail").value("abc"))
                .andExpect(status().isOk());

    }
}
