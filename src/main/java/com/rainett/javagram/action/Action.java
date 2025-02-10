package com.rainett.javagram.action;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Represents an executable action that processes a Telegram update.
 * <p>
 * Implementations of this interface should encapsulate the business logic required
 * to handle a Telegram update. This interface is marked as a {@code FunctionalInterface}
 * to support lambda expressions and method references.
 * </p>
 */
@FunctionalInterface
public interface Action {
    /**
     * Executes this action using the provided Telegram update.
     *
     * @param update the Telegram update to process
     */
    void run(Update update);
}
