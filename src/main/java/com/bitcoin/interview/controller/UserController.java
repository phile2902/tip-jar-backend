package com.bitcoin.interview.controller;

import com.bitcoin.interview.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/user")
public class UserController {
    private final IAuthService authService;

    /**
     * This api to generate apiKey to client
     */
    @GetMapping("/{userId}/generateApiKey")
    public ResponseEntity<String> generateApiKey(@PathVariable(value = "userId") Long userId) throws Exception {
        return new ResponseEntity<>(authService.generateApiKeyToAdmin(userId), HttpStatus.CREATED);
    }
}
