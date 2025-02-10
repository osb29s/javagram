package com.rainett.javagram.action.container;

import com.rainett.javagram.action.Action;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * A container that encapsulates the retrieval of an {@link Action} based on an incoming Telegram
 * {@link Update}.
 * <p>
 * This interface abstracts the logic for matching an update to the appropriate action,
 * allowing implementations to collect, group, and select the correct action handler. If no
 * suitable action is found, implementations are expected to throw an appropriate runtime
 * exception.
 * </p>
 */
public interface ActionContainer {

    /**
     * Finds and returns the {@link Action} that should handle the provided Telegram update.
     * Implementations are expected to throw an exception if no matching action is found.
     *
     * @param update the Telegram update to be handled; must not be {@code null}
     * @return the matching {@link Action} for the update
     */
    Action findByUpdate(Update update);
}
