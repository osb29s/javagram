package com.rainett.javagram.action.plugin.impl.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Text annotation = new TestTextAnnotation(
                testCase.annotation.equals != null ? testCase.annotation.equals : "",
                testCase.annotation.contains != null ? testCase.annotation.contains : "",
                testCase.annotation.startsWith != null ? testCase.annotation.startsWith : "",
                testCase.annotation.endsWith != null ? testCase.annotation.endsWith : "",
                testCase.annotation.regex != null ? testCase.annotation.regex : ""
        );

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

    private static class JsonTestCase {
        public AnnotationData annotation;
        public String text;
        public boolean expected;
    }

    private static class AnnotationData {
        public String equals;
        public String contains;
        public String startsWith;
        public String endsWith;
        public String regex;
    }

    private record TestTextAnnotation(String equals,
                                      String contains,
                                      String startsWith,
                                      String endsWith,
                                      String regex) implements Text {
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
