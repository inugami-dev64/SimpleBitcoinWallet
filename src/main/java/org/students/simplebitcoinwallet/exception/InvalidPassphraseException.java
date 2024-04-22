package org.students.simplebitcoinwallet.exception;

/**
 * This kind of exception is used with symmetric cryptography to demonstrate that something decryption has failed due to invalid passphrase
 */
public class InvalidPassphraseException extends GeneralCryptographyException {
    public InvalidPassphraseException(String message) {
        super(message);
    }
}
