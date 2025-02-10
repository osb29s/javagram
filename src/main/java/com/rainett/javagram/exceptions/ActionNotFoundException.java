package com.rainett.javagram.exceptions;

/**
 * Exception thrown when no action is found to handle a given update.
 * <p>
 * This exception is unchecked because the absence of a matching action
 * typically indicates an unexpected situation or misconfiguration rather than
 * a recoverable condition.
 * </p>
 */
public class ActionNotFoundException extends RuntimeException {
    public ActionNotFoundException(String message) {
        super(message);
    }
}
