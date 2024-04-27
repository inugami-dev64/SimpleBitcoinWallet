package org.students.simplebitcoinwallet.exception;

/**
 * Thrown when wrong passphrase is provided during decryption
 */
public class InvalidKeyException extends GeneralCryptographyException {
    public InvalidKeyException(String msg) {
        super(msg);
    }
}
