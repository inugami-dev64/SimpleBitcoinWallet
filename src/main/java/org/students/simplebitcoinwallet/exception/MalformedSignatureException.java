package org.students.simplebitcoinwallet.exception;

/**
 * This type of exception gets thrown when signature format is malformed and thus verification is not possible
 */
public class MalformedSignatureException extends GeneralCryptographyException {
    public MalformedSignatureException(String message) {
        super(message);
    }
}
