package com.rainett.javagram.action.container.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.annotations.BotAction;
import com.rainett.javagram.action.comparator.ActionComparatorService;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Tests for {@link ActionCollectorImpl}.
 */
class ActionCollectorImplTest {
    @BotAction
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestBotAnnotationA {
    }

    @BotAction
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestBotAnnotationB {
    }

    @TestBotAnnotationA
    public record DummyActionA(int order) implements Action {
        @Override
        public void run(Update update) {
            // No-op for testing.
        }
    }

    @TestBotAnnotationB
    public record DummyActionB(int order) implements Action {
        @Override
        public void run(Update update) {
            // No-op for testing.
        }
    }

    /**
     * Dummy action that does NOT have any annotation meta-annotated with {@link BotAction}.
     * Even though this bean wouldn’t be returned by a real
     * ApplicationContext.getBeansWithAnnotation(BotAction.class),
     * we simulate it by forcing the bean into the returned map.
     */
    public static class DummyActionWithoutBotAction implements Action {
        @Override
        public void run(Update update) {
            // No-op for testing.
        }
    }

    @Test
    void testCollectActions() {
        Map<String, Object> beans = new HashMap<>();
        beans.put("dummyA1", new DummyActionA(2));
        beans.put("dummyA2", new DummyActionA(1));
        beans.put("dummyB1", new DummyActionB(5));
        beans.put("dummyB2", new DummyActionB(3));

        // This bean is not an Action and should be filtered out.
        beans.put("notAction", new Object());

        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBeansWithAnnotation(BotAction.class)).thenReturn(beans);

        ActionComparatorService comparatorService = mock(ActionComparatorService.class);
        Comparator<Action> comparatorA = (a1, a2) -> {
            int order1 = ((DummyActionA) a1).order();
            int order2 = ((DummyActionA) a2).order();
            return Integer.compare(order1, order2);
        };
        Comparator<Action> comparatorB = (a1, a2) -> {
            int order1 = ((DummyActionB) a1).order();
            int order2 = ((DummyActionB) a2).order();
            return Integer.compare(order1, order2);
        };

        when(comparatorService.getActionComparator(TestBotAnnotationA.class)).thenReturn(
                comparatorA);
        when(comparatorService.getActionComparator(TestBotAnnotationB.class)).thenReturn(
                comparatorB);

        ActionCollectorImpl collector = new ActionCollectorImpl(context, comparatorService);
        Map<Class<? extends Annotation>, List<Action>> collectedActions =
                collector.collectActions();

        // Verify that two groups are collected.
        assertEquals(2, collectedActions.size());
        assertTrue(collectedActions.containsKey(TestBotAnnotationA.class));
        assertTrue(collectedActions.containsKey(TestBotAnnotationB.class));

        // Verify sorting for TestBotAnnotationA group.
        List<Action> actionsA = collectedActions.get(TestBotAnnotationA.class);
        assertEquals(2, actionsA.size());
        DummyActionA a1 = (DummyActionA) actionsA.get(0);
        DummyActionA a2 = (DummyActionA) actionsA.get(1);
        assertTrue(a1.order() < a2.order(),
                "Actions for TestBotAnnotationA are not sorted correctly.");

        // Verify sorting for TestBotAnnotationB group.
        List<Action> actionsB = collectedActions.get(TestBotAnnotationB.class);
        assertEquals(2, actionsB.size());
        DummyActionB b1 = (DummyActionB) actionsB.get(0);
        DummyActionB b2 = (DummyActionB) actionsB.get(1);
        assertTrue(b1.order() < b2.order(),
                "Actions for TestBotAnnotationB are not sorted correctly.");
    }

    @Test
    void testExtractAnnotationTypeThrowsExceptionWhenAnnotationNotFound() {
        // Create a beans map that includes an Action with no annotation
        // (or with annotations that are NOT meta-annotated with @BotAction).
        Map<String, Object> beans = new HashMap<>();
        beans.put("dummyNoAnnotation", new DummyActionWithoutBotAction());

        // Although getBeansWithAnnotation(BotAction.class) normally returns
        // only beans with a @BotAction meta-annotation,
        // here we simulate an incorrect configuration by forcing such a bean into the map.
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBeansWithAnnotation(BotAction.class)).thenReturn(beans);

        ActionComparatorService comparatorService = mock(ActionComparatorService.class);
        // The comparator service won't be used because extraction fails first.
        ActionCollectorImpl collector = new ActionCollectorImpl(context, comparatorService);

        IllegalStateException exception =
                assertThrows(IllegalStateException.class, collector::collectActions);
        assertTrue(exception.getMessage().contains("Cannot find required annotation for action"),
                "Expected exception message indicating missing annotation.");
    }
}
