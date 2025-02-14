package com.rainett.javagram.action.plugin.impl.command;

import com.rainett.javagram.action.annotations.Command;
import com.rainett.javagram.action.comparator.AnnotationComparator;

/**
 * Comparator of {@link com.rainett.javagram.action.Action} instances based on the
 * {@link Command} annotation.
 */
public class CommandComparator implements AnnotationComparator<Command> {

    /**
     * Compares two {@link Command} annotations based on their length.
     * @param c1 the first object to be compared.
     * @param c2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as {@code c1} is less than,
     * equal to, or greater than {@code c2}.
     */
    @Override
    public int compare(Command c1, Command c2) {
        return Integer.compare(c1.value().length(), c2.value().length());
    }

    /**
     * The target annotation for this comparator is {@link Command}.
     * @return the target annotation.
     */
    @Override
    public Class<Command> getAnnotationType() {
        return Command.class;
    }
}
