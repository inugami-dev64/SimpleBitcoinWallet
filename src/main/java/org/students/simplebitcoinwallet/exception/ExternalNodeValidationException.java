package org.students.simplebitcoinwallet.exception;

/**
 * Thrown when external node responds with validation errors
 */
public class ExternalNodeValidationException extends ExternalNodeResponseException{
    public ExternalNodeValidationException(String message) {
        super(message);
    }
}
