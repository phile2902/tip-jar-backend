package com.bitcoin.interview.service;

import com.bitcoin.interview.helper.ApiKeyEncryptorDecryptor;
import com.bitcoin.interview.model.Auth;
import com.bitcoin.interview.model.User;
import com.bitcoin.interview.repository.AuthRepository;
import com.bitcoin.interview.repository.UserRepository;
import com.bitcoin.interview.service.exception.AuthNotFoundException;
import com.bitcoin.interview.service.exception.UserNotAdminException;
import com.bitcoin.interview.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService{
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final ApiKeyEncryptorDecryptor apiKeyEncryptorDecryptor;

    @Override
    public String generateApiKeyToAdmin(Long userId) throws Exception{
        checkUserExist(userId);
        checkAdminUser(userId);
        
        User user = userRepository.findById(userId).get();
        
        //Generate an UUID key and store in the table Auth
        String key = apiKeyEncryptorDecryptor.generateUUIDKey();
        Auth auth = new Auth(key, user);
        authRepository.save(auth);
        
        return apiKeyEncryptorDecryptor.encryptApiKey(key);
    }
    
    private void checkUserExist(Long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Not found user");
        }
    }
    
    private void checkAdminUser(Long userId) {
        if (userRepository.findByIdAndIsAdminTrue(userId).isEmpty()) {
            throw new UserNotAdminException("User is not allowed to do this action.");
        }
    }

    @Override
    public User findUserByApiKey(String apiKey) throws Exception {
        //Decrypt the apiKey to uuid key. Then use that key to find out the auth
        String key = apiKeyEncryptorDecryptor.decrypt(apiKey);
        Auth auth = authRepository.findByKey(key).orElseThrow(() -> new AuthNotFoundException("Not found Auth"));
        
        User user = auth.getUser();
        //Check again if user has the admin right or not
        if (!user.getIsAdmin()) {
            throw new UserNotAdminException("User is not allowed to do this action."); 
        }
        
        return user;
    }
}
