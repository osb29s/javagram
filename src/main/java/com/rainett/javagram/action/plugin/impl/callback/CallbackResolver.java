package com.rainett.javagram.action.plugin.impl.callback;

import com.rainett.javagram.action.annotations.Callback;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Resolver for Callback updates, that occur when a user presses a button in a message.
 * Implements the {@link UpdateTypeResolver} interface.
 */
public class CallbackResolver implements UpdateTypeResolver<Callback> {

    /**
     * Checks if the update contains a callback query.
     * @param update the input argument
     * @return {@code true} if the update contains a callback query; {@code false} otherwise
     */
    @Override
    public boolean test(Update update) {
        return update.hasCallbackQuery();
    }

    /**
     * Returns the annotation type associated with the resolver.
     * @return the annotation type
     */
    @Override
    public Class<Callback> getAnnotationType() {
        return Callback.class;
    }
}
