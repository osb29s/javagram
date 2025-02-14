package com.rainett.javagram.action.plugin.impl.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

class TextUpdateMatcherTest {

    private final TextUpdateMatcher textUpdateMatcher = new TextUpdateMatcher();

    static Stream<JsonTestCase> jsonTestCases() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is =
                TextUpdateMatcherTest.class.getResourceAsStream("/textUpdateMatcherTests.json");
        List<JsonTestCase> testCases = mapper.readValue(is, new TypeReference<>() {
        });
        return testCases.stream();
    }

    @ParameterizedTest
    @MethodSource("jsonTestCases")
    void testTextUpdateMatcher(JsonTestCase testCase) {
        Text annotation = new TestTextAnnotation(testCase.annotation);

        Message message = new Message();
        message.setText(testCase.text);
        Update update = new Update();
        update.setMessage(message);
        boolean result = textUpdateMatcher.match(annotation, update);

        assertEquals(testCase.expected, result, "Failed for annotation: "
                                                + annotation + " and update: " + testCase.text);
    }

    @Test
    void returnsTextAnnotationType() {
        assertEquals(Text.class, textUpdateMatcher.getAnnotationType());
    }

    @Data
    private static class JsonTestCase {
        private AnnotationData annotation;
        private String text;
        private boolean expected;
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
