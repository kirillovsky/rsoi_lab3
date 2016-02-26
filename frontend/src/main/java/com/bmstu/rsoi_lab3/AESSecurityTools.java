package com.bmstu.rsoi_lab3;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * Created by Александр on 26.02.2016.
 */

@Component("securityTool")
public class AESSecurityTools implements SecurityTools {

    @Value("${security.encryption.key}")
    private String encryptionKey;

    public AESSecurityTools() {
    }

    @Override
    public String encrypt(String str) {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        byte[] encryptedBytes;
        try {
            encryptedBytes = cipher.doFinal(str.getBytes());
        } catch (IllegalBlockSizeException|BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return Base64.encodeBase64String(encryptedBytes);
    }

    @Override
    public String decrypt(String str) {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        byte[] plainBytes;
        try {
            plainBytes = cipher.doFinal(Base64.decodeBase64(str));
        } catch (IllegalBlockSizeException|BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return new String(plainBytes);
    }

    private Cipher getCipher(int cipherMode) {
        String encryptionAlgorithm = "AES";
        Cipher cipher;
        try {
            SecretKeySpec keySpecification = new SecretKeySpec(encryptionKey.getBytes("UTF-8"),
                    encryptionAlgorithm);
            cipher = Cipher.getInstance(encryptionAlgorithm);
            cipher.init(cipherMode, keySpecification);
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return cipher;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
}
