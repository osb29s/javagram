package com.rainett.javagram.action.actionmatcher;

import com.rainett.javagram.action.Action;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for {@link AbstractActionUpdateMatcher} using a concrete test subclass.
 */
class AbstractActionUpdateMatcherTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        // For simplicity, no members are needed.
    }

    @TestAnnotation
    public static class AnnotatedAction implements Action {
        @Override
        public void run(Update update) {
            // No-op for testing.
        }
    }

    // This action is NOT annotated.
    public static class NonAnnotatedAction implements Action {
        @Override
        public void run(Update update) {
            // No-op for testing.
        }
    }

    /**
     * A simple test implementation that:
     * - Uses TestAnnotation as the annotation type.
     * - Considers any non-null update as a match if the annotation is present.
     */
    public static class TestActionUpdateMatcher
            extends AbstractActionUpdateMatcher<TestAnnotation> {
        @Override
        public Class<TestAnnotation> getAnnotationType() {
            return TestAnnotation.class;
        }

        @Override
        protected boolean match(TestAnnotation annotation, Update update) {
            // In this test, if the annotation is not null and update is not null, return true.
            return annotation != null && update != null;
        }
    }

    @Test
    void testMatcherWithAnnotatedAction() {
        Action action = new AnnotatedAction();
        Update update = new Update();
        TestActionUpdateMatcher matcher = new TestActionUpdateMatcher();
        boolean result = matcher.test(action, update);
        assertTrue(result,
                "Expected matcher to return true for an annotated action with a valid update.");
    }

    @Test
    void testMatcherWithNonAnnotatedAction() {
        Action action = new NonAnnotatedAction();
        Update update = new Update();
        TestActionUpdateMatcher matcher = new TestActionUpdateMatcher();
        boolean result = matcher.test(action, update);
        assertFalse(result, "Expected matcher to return false for a non-annotated action.");
    }

    @Test
    void testMatcherWithNullUpdate() {
        Action action = new AnnotatedAction();
        TestActionUpdateMatcher matcher = new TestActionUpdateMatcher();
        boolean result = matcher.test(action, null);
        assertFalse(result, "Expected matcher to return false when update is null.");
    }
}
