package org.students.simplebitcoinwallet.exception;

import lombok.Getter;
import org.students.simplebitcoinwallet.data.ValidationErrorResponse;

/**
 * Thrown when external node responds with validation errors
 */
@Getter
public class ExternalNodeValidationException extends ExternalNodeResponseException{
    private final ValidationErrorResponse validationErrorResponse;
    public ExternalNodeValidationException(String message, ValidationErrorResponse validationErrorResponse) {
        super(message);
        this.validationErrorResponse = validationErrorResponse;
    }
}
