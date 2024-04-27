package org.students.simplebitcoinwallet.exception;

public class InvalidCipherException extends GeneralCryptographyException {
    public InvalidCipherException(String msg) {
        super(msg);
    }
}
