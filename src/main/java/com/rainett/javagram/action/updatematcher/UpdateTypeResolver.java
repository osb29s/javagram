package com.rainett.javagram.action.updatematcher;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Determines whether a given Telegram {@link Update} matches a specific update type.
 * <p>
 * Implementations of this interface are responsible for inspecting a Telegram update and
 * deciding if it corresponds to a particular type, which is denoted by a custom annotation
 * of type {@code T}. This mechanism is useful for routing or filtering updates based on
 * metadata provided by annotations.
 * </p>
 *
 * @param <T> the type of annotation used to identify the update type.
 */
public interface UpdateTypeResolver<T extends Annotation> extends Predicate<Update> {

    /**
     * Evaluates whether the given Telegram update meets the criteria defined by this resolver.
     *
     * @param update the Telegram update to evaluate
     * @return {@code true} if the update matches the expected criteria; {@code false} otherwise
     */
    @Override
    boolean test(Update update);

    /**
     * Returns the annotation type that is associated with this resolver.
     * <p>
     * This annotation is used to mark actions, that process updates of a specific type.
     * </p>
     *
     * @return the {@link Class} object representing the annotation type {@code T}
     */
    Class<T> getAnnotationType();
}
