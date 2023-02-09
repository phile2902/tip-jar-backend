package com.bitcoin.interview.service;

import com.bitcoin.interview.model.User;

public interface IAuthService {
    User findUserByApiKey(String apiKey) throws Exception;
    
    String generateApiKeyToAdmin(Long userId) throws Exception;
}
