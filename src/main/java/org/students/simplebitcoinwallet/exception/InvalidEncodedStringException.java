package org.students.simplebitcoinwallet.exception;

public class InvalidEncodedStringException extends SerializationException {
    public InvalidEncodedStringException(String msg) {
        super(msg);
    }
}
