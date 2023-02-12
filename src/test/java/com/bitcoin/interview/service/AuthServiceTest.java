package com.bitcoin.interview.service;

import com.bitcoin.interview.helper.ApiKeyEncryptorDecryptor;
import com.bitcoin.interview.model.Auth;
import com.bitcoin.interview.model.User;
import com.bitcoin.interview.repository.AuthRepository;
import com.bitcoin.interview.repository.UserRepository;
import com.bitcoin.interview.service.exception.NotAllowedException;
import com.bitcoin.interview.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository = mock(UserRepository.class);

    @Mock
    private AuthRepository authRepository = mock(AuthRepository.class);

    @Mock
    private ApiKeyEncryptorDecryptor apiKeyEncryptorDecryptor = mock(ApiKeyEncryptorDecryptor.class);

    @InjectMocks
    private AuthService authService;

    @Test
    public void testGenerateApiKeyToAdminNewApiKey() throws Exception {
        Long userId = 1L;
        String key = "testKey";
        String apiKey = "testApiKey";
        User user = mock(User.class);
        Auth savedAuth = mock(Auth.class);

        when(userRepository.findByIdAndIsAdminTrue(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(authRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(apiKeyEncryptorDecryptor.generateUUIDKey()).thenReturn(key);
        when(authRepository.save(any(Auth.class))).thenReturn(savedAuth);
        when(apiKeyEncryptorDecryptor.encryptApiKey(key)).thenReturn(apiKey);

        assertEquals(apiKey, authService.generateApiKeyToAdmin(userId));
    }

    @Test
    public void testGenerateApiKeyToAdminWithExistingApiKey() throws Exception {
        Long userId = 1L;
        String key = "testKey";
        String existingApiKey = "testApiKey";
        User user = mock(User.class);
        Auth existingAuth = mock(Auth.class);
        when(existingAuth.getKey()).thenReturn(key);

        when(userRepository.findByIdAndIsAdminTrue(userId)).thenReturn(Optional.of(user));
        when(authRepository.findByUserId(userId)).thenReturn(Optional.of(existingAuth));
        when(apiKeyEncryptorDecryptor.generateUUIDKey()).thenReturn(key);
        when(apiKeyEncryptorDecryptor.encryptApiKey(key)).thenReturn(existingApiKey);

        assertEquals(existingApiKey, authService.generateApiKeyToAdmin(userId));
    }

    @Test
    public void testGenerateApiKeyToAdminNotFoundAdminUser() throws Exception {
        Long userId = 1L;

        when(userRepository.findByIdAndIsAdminTrue(userId)).thenReturn(Optional.empty());

        assertThrows(NotAllowedException.class, () -> authService.generateApiKeyToAdmin(userId));
    }

    @Test
    public void testGenerateApiKeyToAdminFailedToEncryptKey() throws Exception {
        Long userId = 1L;
        String key = "testKey";
        User user = mock(User.class);
        Auth savedAuth = mock(Auth.class);

        when(userRepository.findByIdAndIsAdminTrue(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(authRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(apiKeyEncryptorDecryptor.generateUUIDKey()).thenReturn(key);
        when(authRepository.save(any(Auth.class))).thenReturn(savedAuth);
        when(apiKeyEncryptorDecryptor.encryptApiKey(key)).thenThrow(Exception.class);

        assertThrows(Exception.class, () -> authService.generateApiKeyToAdmin(userId));
    }

    @Test
    public void testFindUserByApiKey() throws Exception {
        String apiKey = "testApiKey";
        String key = "testKey";

        User user = mock(User.class);
        when(user.getIsAdmin()).thenReturn(true);
        Auth savedAuth = mock(Auth.class);
        when(savedAuth.getUser()).thenReturn(user);

        when(apiKeyEncryptorDecryptor.decrypt(apiKey)).thenReturn(key);
        when(authRepository.findByKey(key)).thenReturn(Optional.of(savedAuth));

        assertEquals(user, authService.findUserByApiKey(apiKey));
    }

    @Test
    public void testFindUserByApiKeyFailedToDecryptKey() throws Exception {
        String apiKey = "testApiKey";

        when(apiKeyEncryptorDecryptor.decrypt(apiKey)).thenThrow(Exception.class);

        assertThrows(Exception.class, () -> authService.findUserByApiKey(apiKey));
    }

    @Test
    public void testFindUserByApiKeyNotFoundAuth() throws Exception {
        String apiKey = "testApiKey";
        String key = "testKey";

        when(apiKeyEncryptorDecryptor.decrypt(apiKey)).thenReturn(key);
        when(authRepository.findByKey(key)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.findUserByApiKey(apiKey));
    }

    @Test
    public void testFindUserByApiKeyUserNotAdmin() throws Exception {
        String apiKey = "testApiKey";
        String key = "testKey";

        User user = mock(User.class);
        when(user.getIsAdmin()).thenReturn(false);
        Auth savedAuth = mock(Auth.class);
        when(savedAuth.getUser()).thenReturn(user);

        when(apiKeyEncryptorDecryptor.decrypt(apiKey)).thenReturn(key);
        when(authRepository.findByKey(key)).thenReturn(Optional.of(savedAuth));

        assertThrows(NotAllowedException.class, () -> authService.findUserByApiKey(apiKey));
    }
}
