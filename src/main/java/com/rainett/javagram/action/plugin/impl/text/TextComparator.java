package com.rainett.javagram.action.plugin.impl.text;

import com.rainett.javagram.action.annotations.Text;
import com.rainett.javagram.action.comparator.AnnotationComparator;

public class TextComparator implements AnnotationComparator<Text> {
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
