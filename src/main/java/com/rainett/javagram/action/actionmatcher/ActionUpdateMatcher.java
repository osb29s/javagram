package com.rainett.javagram.action.actionmatcher;

import com.rainett.javagram.action.Action;
import java.lang.annotation.Annotation;
import java.util.function.BiPredicate;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Interface responsible for matching updates with actions.
 * Extends the {@link BiPredicate} interface for better compatibility.
 * @param <T> the annotation type
 */
public interface ActionUpdateMatcher<T extends Annotation> extends BiPredicate<Action, Update> {
    /**
     * The annotation type supported by this matcher.
     * @return the annotation type
     */
    Class<T> getAnnotationType();
}
