package com.bitcoin.interview.controller;

import com.bitcoin.interview.model.Payment;
import com.bitcoin.interview.model.User;
import com.bitcoin.interview.service.IPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPaymentService paymentService;

    @Test
    public void testDeleteById() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(paymentService).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/managements/payments/%s", id)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatePayment() throws Exception {
        User user = new User();
        Long id = 1L;
        Payment payment = new Payment(2400.0, 240.0, "abc", user);

        when(paymentService.updateById(eq(id), any(Payment.class))).thenReturn(payment);

        mockMvc.perform(
                        MockMvcRequestBuilders.put(String.format("/api/v1/managements/payments/%s", id))
                                .content(new ObjectMapper().writeValueAsString(payment))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(2400.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip").value(240.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail").value("abc"));
    }
}
