package com.rainett.javagram.action.plugin.impl.callback;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.javagram.action.annotations.Callback;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

class CallbackUpdateMatcherTest {
    private final CallbackUpdateMatcher callbackUpdateMatcher = new CallbackUpdateMatcher();

    static Stream<JsonTestData> jsonTestCases() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = CallbackUpdateMatcherTest.class
                .getResourceAsStream("/callbackUpdateMatcherTests.json");
        List<JsonTestData> testCases = mapper.readValue(is, new TypeReference<>() {});
        return testCases.stream();
    }

    @ParameterizedTest
    @MethodSource("jsonTestCases")
    void matchesCallbackUpdates(JsonTestData testData) {
        Callback annotation = new TestAnnotation(testData.annotationData);
        Update update = new Update();
        if (testData.callbackData != null) {
            CallbackQuery callbackQuery = new CallbackQuery();
            callbackQuery.setData(testData.callbackData);
            update.setCallbackQuery(callbackQuery);
        }
        boolean result = callbackUpdateMatcher.match(annotation, update);
        assertEquals(testData.expected, result);
    }

    @Test
    void returnsAnnotationType() {
        assertEquals(Callback.class, callbackUpdateMatcher.getAnnotationType());
    }

    @Data
    private static class JsonTestData {
        private String callbackData;
        private String annotationData;
        private boolean expected;
    }

    @ToString
    @AllArgsConstructor
    private static class TestAnnotation implements Callback {
        private String value;

        @Override
        public String value() {
            return value;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Callback.class;
        }
    }
}