package com.rainett.javagram.update.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Service interface for processing Telegram updates.
 * <p>
 * This interface defines a contract for handling incoming updates from Telegram.
 * Implementations should delegate the update to an appropriate action for processing.
 * </p>
 */
public interface UpdateService {
    /**
     * Processes the given Telegram update by delegating it to the appropriate action.
     *
     * @param update the Telegram update to process
     */
    void handleUpdate(Update update);
}
