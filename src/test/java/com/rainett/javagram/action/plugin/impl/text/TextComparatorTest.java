package com.rainett.javagram.action.plugin.impl.text;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.javagram.action.annotations.Text;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TextComparatorTest {
    private final TextComparator textComparator = new TextComparator();

    static Stream<JsonTestCase> jsonTestCase() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is =
                TextComparatorTest.class.getResourceAsStream("/textComparatorTests.json");
        List<JsonTestCase> testCases = mapper.readValue(is, new TypeReference<>() {
        });
        return testCases.stream();
    }

    @Test
    void returnsTextAnnotationType() {
        assertEquals(Text.class, textComparator.getAnnotationType());
    }

    @ParameterizedTest
    @MethodSource("jsonTestCase")
    void testTextComparing(JsonTestCase testCase) {
        Text annotation1 = new TestTextAnnotation(testCase.annotation1);
        Text annotation2 = new TestTextAnnotation(testCase.annotation2);
        int result = textComparator.compare(annotation1, annotation2);
        if (testCase.expected < 0) {
            assertTrue(result < 0, "Expected negative result: "
                                   + annotation1 + ", " + annotation2);
        } else if (testCase.expected > 0) {
            assertTrue(result > 0, "Expected positive result: "
                                   + annotation1 + ", " + annotation2);
        } else {
            assertEquals(0, result, "Expected zero: "
                                    + annotation1 + ", " + annotation2);
        }
    }

    private static class JsonTestCase {
        public AnnotationData annotation1;
        public AnnotationData annotation2;
        public int expected;
    }

    private static class AnnotationData {
        public String equals;
        public String contains;
        public String startsWith;
        public String endsWith;
        public String regex;
    }

    private static class TestTextAnnotation implements Text {
        private final String equals;
        private final String contains;
        private final String startsWith;
        private final String endsWith;
        private final String regex;

        public TestTextAnnotation(AnnotationData data) {
            this.equals = data.equals;
            this.contains = data.contains;
            this.startsWith = data.startsWith;
            this.endsWith = data.endsWith;
            this.regex = data.regex;
        }

        @Override
        public String equals() {
            return equals;
        }

        @Override
        public String contains() {
            return contains;
        }

        @Override
        public String startsWith() {
            return startsWith;
        }

        @Override
        public String endsWith() {
            return endsWith;
        }

        @Override
        public String regex() {
            return regex;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Text.class;
        }

        @Override
        public String toString() {
            return "TestTextAnnotation{" +
                   "equals='" + equals + '\'' +
                   ", contains='" + contains + '\'' +
                   ", startsWith='" + startsWith + '\'' +
                   ", endsWith='" + endsWith + '\'' +
                   ", regex='" + regex + '\'' +
                   '}';
        }
    }
}