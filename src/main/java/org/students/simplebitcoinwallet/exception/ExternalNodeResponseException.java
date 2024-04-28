package org.students.simplebitcoinwallet.exception;

/**
 * Base exception type for invalid external node responses
 */
public class ExternalNodeResponseException extends Exception {
    public ExternalNodeResponseException(String message) {
        super(message);
    }
}
