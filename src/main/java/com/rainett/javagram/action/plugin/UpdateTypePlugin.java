package com.rainett.javagram.action.plugin;

import com.rainett.javagram.action.actionmatcher.ActionUpdateMatcher;
import com.rainett.javagram.action.comparator.AnnotationComparator;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import java.lang.annotation.Annotation;

/**
 * A plugin that encapsulates all required implementations for a new update type.
 *
 * @param <T> the annotation type that marks this update type (e.g. {@code @TextUpdate})
 */
public interface UpdateTypePlugin<T extends Annotation> {
    /**
     * Returns the annotation class that marks this update type.
     *
     * @return the annotation type
     */
    Class<T> getAnnotationType();

    /**
     * Returns the {@link UpdateTypeResolver} used to resolve if an
     * {@link org.telegram.telegrambots.meta.api.objects.Update} belongs to this type.
     *
     * @return the update type matcher
     */
    UpdateTypeResolver<T> getUpdateTypeMatcher();

    /**
     * Returns the {@link ActionUpdateMatcher} used to test whether an
     * {@link com.rainett.javagram.action.Action} matches an
     * {@link org.telegram.telegrambots.meta.api.objects.Update} for this type.
     *
     * @return the action matcher
     */
    ActionUpdateMatcher<T> getActionMatcher();

    /**
     * Returns the {@link AnnotationComparator} used to sort actions of this update type.
     *
     * @return the action comparator
     */
    AnnotationComparator<T> getAnnotationComparator();
}
