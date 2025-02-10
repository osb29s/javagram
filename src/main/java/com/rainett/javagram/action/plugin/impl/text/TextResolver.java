package com.rainett.javagram.action.plugin.impl.text;

import com.rainett.javagram.action.annotations.Text;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Resolver for identifying Telegram updates that contain textual messages.
 * <p>
 * This implementation of {@link UpdateTypeResolver} checks whether an incoming
 * {@link Update} contains a message and if that message includes text.
 * </p>
 */
public class TextResolver implements UpdateTypeResolver<Text> {

    /**
     * Evaluates whether the given update contains a message with text.
     *
     * @param update the Telegram update to be evaluated
     * @return {@code true} if the update contains a message and the message has text;
     * {@code false} otherwise
     */
    @Override
    public boolean test(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    /**
     * Returns the annotation type that this resolver supports.
     *
     * @return {@code Text.class}, indicating that this resolver is associated
     * with the {@link Text} annotation.
     */
    @Override
    public Class<Text> getAnnotationType() {
        return Text.class;
    }
}
