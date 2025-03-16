package com.rainett.javagram.action.comparator.impl;

import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.comparator.ActionComparatorService;
import com.rainett.javagram.action.comparator.AnnotationComparator;
import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link ActionComparatorService} that selects an
 * {@link AnnotationComparator} based on an annotation type and returns an
 * {@link Action} comparator that compares actions by the value of the annotation.
 */
@Component
public class ActionComparatorServiceImpl implements ActionComparatorService {
    private final Map<Class<? extends Annotation>, AnnotationComparator<?>> annotationComparators;

    /**
     * Constructs a new instance with a list of available {@link AnnotationComparator}
     * implementations.
     *
     * @param comparatorsList a list of comparators; must not be {@code null} and must not
     *                        contain {@code null} elements.
     * @throws NullPointerException if {@code comparatorsList} is {@code null}
     */
    public ActionComparatorServiceImpl(List<AnnotationComparator<?>> comparatorsList) {
        Objects.requireNonNull(comparatorsList, "comparatorsList must not be null");
        this.annotationComparators = comparatorsList.stream()
                .collect(Collectors.toMap(AnnotationComparator::getAnnotationType,
                        Function.identity()));
    }

    /**
     * Returns a comparator for {@link Action} objects based on the given annotation type.
     * <p>
     * The returned comparator extracts the specified annotation from the {@link Action} classes
     * and then uses the corresponding {@link AnnotationComparator} to compare the annotations.
     *
     * @param annotationType the type of annotation to compare; must not be {@code null}.
     * @param <T>            the annotation type.
     * @return a comparator for {@link Action} objects.
     * @throws NoSuchElementException if no comparator is found for the given annotation type.
     * @throws IllegalStateException  if the found comparator is not of the expected type.
     */
    @Override
    public <T extends Annotation> Comparator<Action> getActionComparator(
            Class<T> annotationType) {
        Objects.requireNonNull(annotationType, "annotationType must not be null");
        AnnotationComparator<T> annotationComparator = getComparatorOrThrow(annotationType);
        return createActionComparator(annotationType, annotationComparator);
    }

    /**
     * Retrieves the {@link AnnotationComparator} for the given annotation type or throws an
     * exception if not found.
     *
     * @param annotationType the annotation type for which to retrieve the comparator.
     * @param <T>            the annotation type.
     * @return the corresponding {@link AnnotationComparator}.
     * @throws NoSuchElementException if no comparator is found for the given annotation type.
     * @throws IllegalStateException  if the comparator is not of the expected type.
     */
    @SuppressWarnings("unchecked")
    private <T extends Annotation> AnnotationComparator<T> getComparatorOrThrow(
            Class<T> annotationType) {
        AnnotationComparator<?> comparator = annotationComparators.get(annotationType);
        if (comparator == null) {
            throw new NoSuchElementException(
                    String.format("Action comparator not found for annotation type [%s]",
                            annotationType));
        }
        return (AnnotationComparator<T>) comparator;
    }

    /**
     * Creates an {@link Action} comparator that uses the given annotation comparator.
     *
     * @param annotationClass      the annotation type to extract from the {@code Action}.
     * @param annotationComparator the comparator for the annotation.
     * @param <T>                  the annotation type.
     * @return a comparator for {@link Action} objects.
     */
    private static <T extends Annotation> Comparator<Action> createActionComparator(
            Class<T> annotationClass, AnnotationComparator<T> annotationComparator) {
        return (action1, action2) -> {
            T annotation1 = extractAnnotation(action1, annotationClass);
            T annotation2 = extractAnnotation(action2, annotationClass);
            return annotationComparator.compare(annotation1, annotation2);
        };
    }

    /**
     * Extracts the specified annotation from the given {@link Action} object.
     *
     * @param action          the action from which to extract the annotation;
     *                        must not be {@code null}.
     * @param annotationClass the annotation class to look for.
     * @param <T>             the annotation type.
     * @return the annotation found on the action’s class.
     * @throws IllegalArgumentException if the action is {@code null} or the annotation
     *                                  is not present.
     */
    private static <T extends Annotation> T extractAnnotation(Action action,
                                                              Class<T> annotationClass) {
        T annotation = AnnotationUtils.findAnnotation(action.getClass(), annotationClass);
        if (annotation == null) {
            throw new IllegalArgumentException(String.format(
                    "Expected annotation [%s] not found on action class [%s].",
                    annotationClass.getSimpleName(), action.getClass().getName()));
        }
        return annotation;
    }
}

