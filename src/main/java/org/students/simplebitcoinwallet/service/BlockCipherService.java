package org.students.simplebitcoinwallet.service;

import org.students.simplebitcoinwallet.exception.InvalidCipherException;
import org.students.simplebitcoinwallet.exception.InvalidKeyException;

/**
 * Interface for providing support for block cipher cryptography abstractions
 */
public interface BlockCipherService {

    /**
     * Perform a block cipher encryption on provided byte array with a key, derived from a passphrase
     * @param msg specifies the array of bytes to encrypt
     * @param passphrase specifies the passphrase to use for key derivation
     * @return byte array containing encrypted cipher
     */
    byte[] encrypt(byte[] msg, String passphrase);


    /**
     * Perform a block cipher decryption on provided cipher data with a key derived from a passphrase
     * @param cipherMsg specifies the cipher to decrypt
     * @param passphrase specifies the passphrase to use for key derivation
     * @return byte array containing decrypted message
     */
    byte[] decrypt(byte[] cipherMsg, String passphrase) throws InvalidKeyException, InvalidCipherException;
}
