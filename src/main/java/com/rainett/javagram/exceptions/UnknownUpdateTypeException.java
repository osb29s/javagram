package com.rainett.javagram.exceptions;

/**
 * Exception thrown when an update of an unknown or unsupported type is encountered.
 * <p>
 * This exception indicates that the received update cannot be processed due to
 * its type, which might be due to an unanticipated input or misconfiguration.
 * </p>
 */
public class UnknownUpdateTypeException extends RuntimeException {
    /**
     * Constructs a new UnknownUpdateTypeException with the specified error message.
     * @param message the error message
     */
    public UnknownUpdateTypeException(String message) {
        super(message);
    }
}
