package com.rainett.javagram.action.plugin.impl.text;

import com.rainett.javagram.action.annotations.Text;
import com.rainett.javagram.action.comparator.AnnotationComparator;

/**
 * Comparator for {@link Text} annotations.
 * This comparator is used to compare {@code Text} annotations, prioritizing properties such as
 * exact match, prefix, suffix, and substring conditions.
 *
 */
public class TextComparator implements AnnotationComparator<Text> {

    /**
     * Compares two {@link Text} annotations based on their properties.
     * First, it compares the exact match, then the REGEX.
     * If nothing is found, it compares the prefix, suffix, and substring conditions.
     * Prefix and suffix conditions are prioritized, as they are more specific.
     *
     * @param t1 the first object to be compared.
     * @param t2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as {@code t1} is less than,
     * equal to, or greater than {@code t2}.
     */
    @Override
    public int compare(Text t1, Text t2) {
        int compareEquals = compareProperty(t1.equals(), t2.equals());
        if (compareEquals != 0) {
            return compareEquals;
        }

        int compareRegex = compareProperty(t1.regex(), t2.regex());
        if (compareRegex != 0) {
            return compareRegex;
        }
        int compareStarts = compareProperty(t1.startsWith(), t2.startsWith());
        int compareEnds = compareProperty(t1.endsWith(), t2.endsWith());
        int compareContains = compareProperty(t1.contains(), t2.contains());
        if (compareStarts != 0) {
            compareStarts *= 3;
        }
        if (compareEnds != 0) {
            compareEnds *= 3;
        }
        return compareStarts + compareEnds + compareContains;
    }

    /**
     * Returns the annotation type supported by this comparator.
     * @return the annotation type supported by this comparator
     */
    @Override
    public Class<Text> getAnnotationType() {
        return Text.class;
    }

    private int compareProperty(String s1, String s2) {
        if (!s1.isEmpty() && s2.isEmpty()) {
            return 1;
        }
        if (s1.isEmpty() && !s2.isEmpty()) {
            return -1;
        }
        if (!s1.isEmpty()) {
            return Integer.compare(s1.length(), s2.length());
        }
        return 0;
    }
}
