package org.students.simplebitcoinwallet.service.impl;

import org.students.simplebitcoinwallet.exception.InvalidCipherException;
import org.students.simplebitcoinwallet.service.BlockCipherService;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import org.students.simplebitcoinwallet.exception.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class RijndaelBlockCipherService implements BlockCipherService {
    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 65536;
    private static final int IV_LENGTH = 16;
    private static final int SALT_LENGTH = 32;
    /**
     * Performs Rijndael encryption on user provided message using cipher block chaining operation mode
     * @param msg specifies the message to encrypt
     * @param passphrase specifies the user provided key to use as basis for key derivation
     * @return a byte array containing initialization vector, key salt and cipher text
     * @throws Exception
     */
    @Override
    public byte[] encrypt(byte[] msg, String passphrase) {
        SecureRandom secureRandom = new SecureRandom();

        // generate initialization vector IvParameterSpec object using random 16 bytes
        byte[] iv = new byte[IV_LENGTH];
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // generate 32 bytes of random data as salt to use for in key derivation
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        try {
            // derive the secret key for performing message encryption
            SecretKeySpec secretKeySpec = deriveSecret(passphrase, salt);

            // instantiate the Cipher object
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // encrypt the message with derived secret key
            byte[] cipherText = cipher.doFinal(msg);
            byte[] encryptedData = new byte[iv.length + salt.length + cipherText.length];

            // finalize the array of encrypted bytes as following format [IV, Salt, CipherText]
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(salt, 0, encryptedData, iv.length, salt.length);
            System.arraycopy(cipherText, 0, encryptedData, iv.length + salt.length, cipherText.length);

            return encryptedData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] cipherMsg, String passphrase) throws InvalidKeyException, InvalidCipherException {
        if (cipherMsg.length < IV_LENGTH + SALT_LENGTH)
            throw new InvalidCipherException("Cipher message length must be atleast " + (IV_LENGTH + SALT_LENGTH));
        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(cipherMsg, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        byte[] salt = new byte[SALT_LENGTH];
        System.arraycopy(cipherMsg, iv.length, salt, 0, salt.length);

        byte[] cipherData = new byte[cipherMsg.length - iv.length - salt.length];
        System.arraycopy(cipherMsg, iv.length + salt.length, cipherData, 0, cipherMsg.length -iv.length - salt.length);

        try {
            SecretKeySpec secretKeySpec = deriveSecret(passphrase, salt);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            return cipher.doFinal(cipherData);
        } catch (BadPaddingException e) {
            throw new InvalidKeyException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private SecretKeySpec deriveSecret(String passphrase, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKey secretKey = factory.generateSecret(spec);
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }
}
