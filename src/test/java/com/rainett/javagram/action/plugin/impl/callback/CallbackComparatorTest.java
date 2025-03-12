package com.rainett.javagram.action.plugin.impl.callback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rainett.javagram.action.annotations.Callback;
import java.lang.annotation.Annotation;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

class CallbackComparatorTest {
    private final CallbackComparator callbackComparator = new CallbackComparator();

    @Test
    void comparesCallbackAnnotations() {
        Callback annotation1 = new TestCallbackAnnotation("value1");
        Callback annotation2 = new TestCallbackAnnotation("value2");
        assertEquals(0, callbackComparator.compare(annotation1, annotation2));
        annotation2 = new TestCallbackAnnotation("short");
        assertTrue(callbackComparator.compare(annotation1, annotation2) > 0);
        annotation1 = new TestCallbackAnnotation("s");
        assertTrue(callbackComparator.compare(annotation1, annotation2) < 0);
    }

    @Test
    void returnsAnnotationType() {
        assertEquals(Callback.class, callbackComparator.getAnnotationType());
    }

    @ToString
    @AllArgsConstructor
    private static class TestCallbackAnnotation implements Callback {
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
