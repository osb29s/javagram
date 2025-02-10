package com.rainett.javagram.action.comparator;

import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.annotations.BotAction;
import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A service that provides comparators for {@link Action} instances based on a specified
 * annotation type. Implementations of this interface are responsible for selecting and returning
 * the appropriate {@code Comparator} that compares actions by extracting a particular annotation
 * from each action's class.
 *
 * @see BotAction
 */
public interface ActionComparatorService {

    /**
     * Returns a comparator for {@link Action} objects associated with the given annotation type.
     * The comparator compares actions by extracting the annotation of the specified type from
     * the action's class.
     *
     * @param annotationType the type of annotation used for comparison; must not be {@code null}.
     * @param <T>            the annotation type.
     * @return a comparator for {@link Action} objects.
     * @throws NoSuchElementException if a comparator for the given annotation type cannot
     * be found.
     * @throws IllegalStateException  if the found comparator is not of the expected type.
     */
    <T extends Annotation> Comparator<Action> getActionComparator(Class<T> annotationType);
}
