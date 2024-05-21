package org.students.simplebitcoinwallet.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.students.simplebitcoinwallet.exception.InvalidCipherException;
import org.students.simplebitcoinwallet.exception.InvalidKeyException;
import org.students.simplebitcoinwallet.service.BlockCipherService;
import org.students.simplebitcoinwallet.service.impl.RijndaelBlockCipherService;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class RijndaelBlockCipherServiceTests {
    private final BlockCipherService blockCipherService = new RijndaelBlockCipherService();
    @Test
    @DisplayName("Ensure that decrypted data is the same as source data")
    void testDataIntegrity() throws Exception {
        final String msg = "Hello World!";
        final String passphrase = "password123";
        byte[] encryptedData = blockCipherService.encrypt(msg.getBytes(StandardCharsets.UTF_8), passphrase);
        byte[] decryptedData = blockCipherService.decrypt(encryptedData, passphrase);
        assertEquals(msg,new String(decryptedData, StandardCharsets.UTF_8));

    }
    @Test
    @DisplayName("Ensure that encrypted data can not be decrypted with invalid key")
    void testDecryptionWithInvalidKey_ExpectThrows() throws Exception {
        final String msg = "Hello World!";
        final String passphrase = "password123";
        byte[] encryptedData = blockCipherService.encrypt(msg.getBytes(StandardCharsets.UTF_8), passphrase);
        assertThrows(InvalidKeyException.class,() -> {
            blockCipherService.decrypt(encryptedData, "valeparool");
        });

    }
    @Test
    @DisplayName("Ensure that encryption and decryption works with empty byte array")
    void testEncryptionWithEmptyByteArray() throws Exception {
        final byte[] msg = new byte[0];
        final String passphrase = "password123";
        byte[] encryptedData = blockCipherService.encrypt(msg, passphrase);
        byte[] decryptedData = blockCipherService.decrypt(encryptedData, passphrase);
        assertArrayEquals(msg, decryptedData);
    }
    @Test
    @DisplayName("Test decryption with empty data")
    void testDecryptionWithEmptyData() throws Exception {
        final byte[] msg = new byte[0];
        final String passphrase = "password123";
        assertThrows(InvalidCipherException.class, () -> {
            blockCipherService.decrypt(msg, passphrase);
        });
    }
}
