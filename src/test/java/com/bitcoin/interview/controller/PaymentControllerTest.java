package com.bitcoin.interview.controller;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.model.User;
import com.bitcoin.interview.service.IPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPaymentService paymentService;

    @Test
    public void TestGetLatestPayment() throws Exception {
        User user = new User();
        Long userId = 1L;
        Payment payment = new Payment(2400.0, 240.0, "abc", user);

        when(paymentService.getLatestByUserId(anyLong())).thenReturn(payment);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/user/%s/payments/latest", userId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(2400.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip").value(240.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail").value("abc"))
                .andExpect(status().isOk());
    }

    @Test
    public void TestGetAll() throws Exception {
        User user = new User();
        Long userId = 1L;
        Payment payment01 = new Payment(2400.0, 240.0, "abc", user);
        Payment payment02 = new Payment(2300.0, 230.0, "abc", user);
        List<Payment> list = new ArrayList<>();
        list.add(payment01);
        list.add(payment02);

        when(paymentService.getAllByUserId(anyLong())).thenReturn(list);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/user/%s/payments", userId)))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<Payment> payments = Arrays.asList(mapper.readValue(result.getResponse().getContentAsString(), Payment[].class));
        Assert.assertEquals(payments.size(), 2);
    }

    @Test
    public void TestCreatePayment() throws Exception {
        User user = new User();
        Long userId = 1L;
        Payment payment = new Payment(2400.0, 240.0, "abc", user);

        when(paymentService.createByUserId(anyLong(), any(Payment.class))).thenReturn(payment);

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
}
