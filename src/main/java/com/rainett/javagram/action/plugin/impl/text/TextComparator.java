package com.rainett.javagram.action.plugin.impl.text;

import com.rainett.javagram.action.annotations.Text;
import com.rainett.javagram.action.comparator.AnnotationComparator;
import java.util.List;
import java.util.function.Function;

/**
 * Comparator for {@link Text} annotations.
 * <p>
 * This comparator compares two {@code Text} annotations based on the following criteria:
 * <ol>
 *   <li>First, by the {@code caseSensitive} flag.</li>
 *   <li>If the flags are equal, by the textual properties in a fixed order:
 *     <ul>
 *       <li>{@code equals}</li>
 *       <li>{@code startsWith}</li>
 *       <li>{@code endsWith}</li>
 *       <li>{@code contains}</li>
 *     </ul>
 *   </li>
 * </ol>
 * <p>
 * For each property the following rules are applied:
 * <ul>
 *   <li>If one annotation provides a non-empty value while the other provides an empty value,
 *   the non-empty value is considered greater.</li>
 *   <li>If both provide a non-empty value, they are compared lexicographically.</li>
 *   <li>If both are empty, that property is skipped.</li>
 * </ul>
 * </p>
 */
public class TextComparator implements AnnotationComparator<Text> {
    private static final List<Function<Text, String>> PROPERTY_EXTRACTORS = List.of(
            Text::equals,
            Text::startsWith,
            Text::endsWith,
            Text::contains
    );

    /**
     * Compares two {@link Text} annotations.
     * <p>
     * First, the {@code caseSensitive} flag is compared. If they differ, that result is returned.
     * Otherwise, the annotations are compared by their textual properties using
     * {@link #compareByProperties(Text, Text)}.
     * </p>
     *
     * @param t1 the first {@code Text} annotation.
     * @param t2 the second {@code Text} annotation.
     * @return a negative integer, zero, or a positive integer as {@code t1} is less than,
     * equal to, or greater than {@code t2}.
     */
    @Override
    public int compare(Text t1, Text t2) {
        int caseSensitiveComparison = Boolean.compare(t1.caseSensitive(), t2.caseSensitive());
        if (caseSensitiveComparison != 0) {
            return caseSensitiveComparison;
        }
        return compareByProperties(t1, t2);
    }

    /**
     * Returns the target annotation type supported by this comparator.
     *
     * @return {@code Text.class}.
     */
    @Override
    public Class<Text> getAnnotationType() {
        return Text.class;
    }

    /**
     * Compares two {@link Text} annotations by evaluating their string properties in a fixed
     * order: {@code equals}, {@code startsWith}, {@code endsWith}, {@code contains}.
     * <p>
     * For each property:
     * <ul>
     *   <li>If one property's value is non-empty while the other is empty, the non-empty one is
     *   considered greater.</li>
     *   <li>If both values are non-empty, they are compared lexicographically.</li>
     *   <li>If both values are empty, that property is skipped (i.e. no decision is made).</li>
     * </ul>
     * If all properties are skipped or yield equality, zero is returned.
     * </p>
     *
     * @param a the first {@code Text} annotation.
     * @param b the second {@code Text} annotation.
     * @return a negative integer, zero, or a positive integer as determined by the property
     * comparisons.
     */
    private int compareByProperties(Text a, Text b) {
        for (Function<Text, String> extractor : PROPERTY_EXTRACTORS) {
            Integer result = compareProperty(extractor.apply(a), extractor.apply(b));
            if (result != null) {
                return result;
            }
        }
        return 0;
    }

    /**
     * Compares two string properties.
     * <p>
     * The rules for comparison are:
     * <ul>
     *   <li>If one string is non-empty while the other is empty, the non-empty string is
     *   considered greater.</li>
     *   <li>If both strings are non-empty, they are compared lexicographically.</li>
     *   <li>If both strings are empty, {@code null} is returned to indicate that this property
     *   should be skipped.</li>
     * </ul>
     * </p>
     *
     * @param s1 the first property string.
     * @param s2 the second property string.
     * @return a negative integer, zero, or a positive integer as {@code s1} is less than,
     * equal to, or greater than {@code s2};
     *         or {@code null} if both strings are empty.
     */
    private static Integer compareProperty(String s1, String s2) {
        boolean s1Provided = !s1.isEmpty();
        boolean s2Provided = !s2.isEmpty();

        if (s1Provided && !s2Provided) {
            return 1;
        } else if (!s1Provided && s2Provided) {
            return -1;
        } else if (!s1Provided) {
            return null;
        } else {
            return s1.compareTo(s2);
        }
    }
}
