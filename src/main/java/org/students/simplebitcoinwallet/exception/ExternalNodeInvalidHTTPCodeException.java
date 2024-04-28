package org.students.simplebitcoinwallet.exception;

import lombok.Getter;

/**
 * Thrown when HTTP code is not 2xx
 */
@Getter
public class ExternalNodeInvalidHTTPCodeException extends ExternalNodeResponseException {
    private final int httpCode;
    public ExternalNodeInvalidHTTPCodeException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }
}
