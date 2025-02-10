package com.rainett.javagram.action.actionmatcher;

import com.rainett.javagram.action.Action;
import java.lang.annotation.Annotation;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Abstract class, extends the {@link ActionUpdateMatcher} interface
 * and provides encapsulates the common logic for matching updates with actions.
 *
 * @param <T> the annotation type
 */
public abstract class AbstractActionUpdateMatcher<T extends Annotation>
        implements ActionUpdateMatcher<T> {

    @Override
    public boolean test(Action action, Update update) {
        T annotation = action.getClass().getAnnotation(getAnnotationType());
        return match(annotation, update);
    }

    /**
     * Matches action's annotation and update
     * @param annotation action's annotation
     * @param update received update from Telegram
     * @return {@code true} if the update matches the annotation; {@code false} otherwise
     */
    protected abstract boolean match(T annotation, Update update);
}
