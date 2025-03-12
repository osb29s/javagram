package com.rainett.javagram.action.plugin.impl.callback;

import com.rainett.javagram.action.annotations.Callback;
import com.rainett.javagram.action.comparator.AnnotationComparator;

/**
 * Comparator for {@link Callback} annotations.
 * Implements the {@link AnnotationComparator} interface to compare {@link Callback} annotations.
 */
public class CallbackComparator implements AnnotationComparator<Callback> {
    /**
     * Compares two {@link Callback} annotations based on their length.
     * @param a1 the first object to be compared.
     * @param a2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as {@code a1} is less than,
     * equal to, or greater than {@code a2}.
     */
    @Override
    public int compare(Callback a1, Callback a2) {
        return Integer.compare(a1.value().length(), a2.value().length());
    }

    /**
     * Returns the annotation type supported by this comparator.
     * @return the annotation type
     */
    @Override
    public Class<Callback> getAnnotationType() {
        return Callback.class;
    }
}
