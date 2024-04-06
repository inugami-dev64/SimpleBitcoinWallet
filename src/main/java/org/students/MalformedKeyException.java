package org.students;

/**
 * Type of exception that gets thrown if cryptographic keys are encoded improperly
 */
public class MalformedKeyException extends GeneralCryptographyException {
    public MalformedKeyException(String msg) {
        super(msg);
    }
}
