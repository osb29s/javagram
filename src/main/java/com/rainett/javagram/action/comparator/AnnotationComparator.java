package com.rainett.javagram.action.comparator;

import java.lang.annotation.Annotation;
import java.util.Comparator;

/**
 * Interface responsible for comparing annotations of the same type.
 * Extends {@link Comparator} for better compatibility.
 * @param <T> the annotation type
 */
public interface AnnotationComparator<T extends Annotation> extends Comparator<T> {
    int compare(T o1Annotation, T o2Annotation);

    /**
     * The annotation type, comparator is responsible for.
     * @return the annotation type
     */
    Class<T> getAnnotationType();
}
