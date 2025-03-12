package com.rainett.javagram.action.plugin.impl.callback;

import com.rainett.javagram.action.actionmatcher.AbstractActionUpdateMatcher;
import com.rainett.javagram.action.annotations.Callback;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Matcher for {@link Callback} updates.
 * Implements {@link AbstractActionUpdateMatcher}.
 * Uses callback-key from callback_data field.
 */
public class CallbackUpdateMatcher extends AbstractActionUpdateMatcher<Callback> {
    private static final String DATA_DELIMITER = ":";

    /**
     * Matcher of annotation and update. Returns true if update has
     * the same callback key as the annotation.
     * @param annotation action's annotation
     * @param update received update from Telegram
     * @return true if update has the same callback key as the annotation
     */
    @Override
    protected boolean match(Callback annotation, Update update) {
        if (annotation.value().isEmpty()) {
            return true;
        }
        String callbackKey = extractCallbackKey(update);
        String annotationKey = annotation.value();
        return callbackKey.equals(annotationKey);
    }

    private String extractCallbackKey(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        if (callbackData.contains(DATA_DELIMITER)) {
            return callbackData.split(DATA_DELIMITER)[0];
        }
        return callbackData;
    }

    /**
     * Returns annotation type supported by this matcher
     * @return annotation type
     */
    @Override
    public Class<Callback> getAnnotationType() {
        return Callback.class;
    }
}
