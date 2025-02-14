package com.rainett.javagram.action.plugin.impl.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.javagram.action.annotations.Command;
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

class CommandComparatorTest {
    private final CommandComparator commandComparator = new CommandComparator();

    static Stream<JsonTestCase> jsonTestCase() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is =
                CommandComparatorTest.class.getResourceAsStream("/commandComparatorTests.json");
        List<JsonTestCase> testCases = mapper.readValue(is, new TypeReference<>() {
        });
        return testCases.stream();
    }

    @Test
    void returnsTextAnnotationType() {
        assertEquals(Command.class, commandComparator.getAnnotationType());
    }

    @ParameterizedTest
    @MethodSource("jsonTestCase")
    void testTextComparing(JsonTestCase testCase) {
        Command annotation1 = new TestCommandAnnotation(testCase.annotation1);
        Command annotation2 = new TestCommandAnnotation(testCase.annotation2);
        int result = commandComparator.compare(annotation1, annotation2);
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
        private String value;
    }

    @ToString
    private static class TestCommandAnnotation implements Command {
        private final String value;

        public TestCommandAnnotation(AnnotationData data) {
            this.value = data.value;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Text.class;
        }
    }
}
