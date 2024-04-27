package org.students.simplebitcoinwallet.service;

/**
 * Interface for providing support for block cipher cryptography abstractions
 */
public interface BlockCipherService {
    /**
     * Calculates implementation specific checksum from given byte array to use for verification
     * @param msg specifies the message to use for checksum calculation
     * @return long value containing the checksum
     */
    long checksum(byte[] msg);

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
    byte[] decrypt(byte[] cipherMsg, String passphrase);
}
