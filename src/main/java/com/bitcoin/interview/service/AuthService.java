package com.bitcoin.interview.service;

import com.bitcoin.interview.helper.ApiKeyEncryptorDecryptor;
import com.bitcoin.interview.model.Auth;
import com.bitcoin.interview.model.User;
import com.bitcoin.interview.repository.AuthRepository;
import com.bitcoin.interview.repository.UserRepository;
import com.bitcoin.interview.service.exception.NotAllowedException;
import com.bitcoin.interview.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final ApiKeyEncryptorDecryptor apiKeyEncryptorDecryptor;

    @Override
    public String generateApiKeyToAdmin(Long userId) throws Exception {
        //Check if user is admin user
        checkAdminUser(userId);

        Optional<Auth> existedAuth = authRepository.findByUserId(userId);

        //if the key is not existed, we generate a new key for client
        //but if the key is existed already, we will return that key
        String key = null;
        if (existedAuth.isEmpty()) {
            User user = userRepository.findById(userId).get();
            //Generate an UUID key and store in the table Auth
            key = apiKeyEncryptorDecryptor.generateUUIDKey();
            Auth auth = new Auth(key, user);
            authRepository.save(auth);
        } else {
            key = existedAuth.get().getKey();
        }

        return apiKeyEncryptorDecryptor.encryptApiKey(key);
    }

    private void checkAdminUser(Long userId) {
        if (userRepository.findByIdAndIsAdminTrue(userId).isEmpty()) {
            throw new NotAllowedException("User is not allowed to do this action.");
        }
    }

    @Override
    @Transactional
    public User findUserByApiKey(String apiKey) throws Exception {
        //Decrypt the apiKey to uuid key. Then use that key to find out the auth
        String key = apiKeyEncryptorDecryptor.decrypt(apiKey);
        Auth auth = authRepository.findByKey(key).orElseThrow(() -> new ResourceNotFoundException("Not found Auth with apiKey: " + apiKey));

        User user = auth.getUser();
        //Check again if user has the admin right or not
        if (!user.getIsAdmin()) {
            throw new NotAllowedException("User is not allowed to do this action.");
        }

        return user;
    }
}
