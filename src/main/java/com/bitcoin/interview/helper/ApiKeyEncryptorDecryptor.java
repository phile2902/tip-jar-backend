package com.bitcoin.interview.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApiKeyEncryptorDecryptor {

    private static final String ALGORITHM = "AES";

    @Value("${auth.secret}")
    private String secretKey;

    public String generateUUIDKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * This method to encrypt they key with the secret and algorithm and return a encrypted key.
     * We use this method to generate the apiKey to the user
     */
    public String encryptApiKey(String uuidKey) throws Exception {
        byte[] keys = secretKey.getBytes(StandardCharsets.UTF_8);
        Key key = new SecretKeySpec(keys, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(uuidKey.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * This method to return the string decrypted according to a secret
     * We use this method to decrypt the api key from client and get the key stored in Auth table
     */
    public String decrypt(String encryptedApiKey) throws Exception {
        byte[] keys = secretKey.getBytes(StandardCharsets.UTF_8);
        Key key = new SecretKeySpec(keys, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedApiKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }
}
