package com.bitcoin.interview.controller;

import com.bitcoin.interview.service.IAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAuthService authService;

    @Test
    public void testGenerateApiKey() throws Exception {
        Long userId = 1L;
        String apiKey = "testApiKey";

        when(authService.generateApiKeyToAdmin(userId)).thenReturn(apiKey);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/user/%s/generateApiKey", userId)))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(apiKey, result.getResponse().getContentAsString());
    }
}
