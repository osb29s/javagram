package com.rainett.javagram.action.plugin.impl.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.javagram.action.annotations.Text;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Stream;
import lombok.Data;
import lombok.ToString;
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

    @Data
    private static class JsonTestCase {
        private AnnotationData annotation1;
        private AnnotationData annotation2;
        private int expected;
    }

    @Data
    private static class AnnotationData {
        private String equals;
        private String contains;
        private String startsWith;
        private String endsWith;
        private String regex;
    }

    @ToString
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
    }
}
