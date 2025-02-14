package com.rainett.javagram.action.comparator.impl;

import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.comparator.AnnotationComparator;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ActionComparatorServiceImpl.
 */
class ActionComparatorServiceImplTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        int order() default 0;
    }

    public static class TestAnnotationComparator implements AnnotationComparator<TestAnnotation> {
        @Override
        public Class<TestAnnotation> getAnnotationType() {
            return TestAnnotation.class;
        }

        @Override
        public int compare(TestAnnotation a1, TestAnnotation a2) {
            return Integer.compare(a1.order(), a2.order());
        }
    }

    @TestAnnotation(order = 5)
    public static class DummyAction1 implements Action {
        @Override
        public void run(Update update) {
            // no-op
        }
    }

    @TestAnnotation(order = 3)
    public static class DummyAction2 implements Action {
        @Override
        public void run(Update update) {
            // no-op
        }
    }

    // An action without the TestAnnotation.
    public static class NonAnnotatedAction implements Action {
        @Override
        public void run(Update update) {
            // no-op
        }
    }

    @Test
    void testGetActionComparatorReturnsComparator() {
        ActionComparatorServiceImpl service = new ActionComparatorServiceImpl(
                List.of(new TestAnnotationComparator())
        );

        Comparator<Action> comparator = service.getActionComparator(TestAnnotation.class);
        assertNotNull(comparator, "Comparator should not be null");
        Action action1 = new DummyAction1();
        Action action2 = new DummyAction2();

        int result = comparator.compare(action1, action2);
        assertTrue(result > 0,
                "Comparator should order actions based on TestAnnotation.order");
    }

    @Test
    void testGetActionComparatorForUnknownAnnotationThrowsException() {
        ActionComparatorServiceImpl service = new ActionComparatorServiceImpl(
                List.of(new TestAnnotationComparator())
        );

        Class<? extends Annotation> unknownAnnotation = Deprecated.class;
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                service.getActionComparator(unknownAnnotation)
        );
        assertTrue(exception.getMessage().contains("Action comparator not found"),
                "Exception message should indicate missing comparator");
    }

    @Test
    void testComparatorThrowsExceptionForNonAnnotatedAction() {
        ActionComparatorServiceImpl service = new ActionComparatorServiceImpl(
                List.of(new TestAnnotationComparator())
        );

        Comparator<Action> comparator = service.getActionComparator(TestAnnotation.class);
        Action action = new NonAnnotatedAction();
        DummyAction1 dummyAction1 = new DummyAction1();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                comparator.compare(action, dummyAction1)
        );
        assertTrue(exception.getMessage().contains("Expected annotation"),
                "Exception message should indicate the missing annotation");
    }

    @Test
    void testGetActionComparatorNullAnnotationThrowsException() {
        ActionComparatorServiceImpl service = new ActionComparatorServiceImpl(
                List.of(new TestAnnotationComparator())
        );

        assertThrows(NullPointerException.class, () -> service.getActionComparator(null));
    }
}
