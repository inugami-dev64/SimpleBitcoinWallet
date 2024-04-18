package org.students.simplebitcoinwallet.exception;

public class SerializationException extends InvalidEncodedStringException {
    public SerializationException(String msg) {
        super(msg);
    }
}
