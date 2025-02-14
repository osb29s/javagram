package com.rainett.javagram.action.container.impl;

import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.actionmatcher.ActionUpdateMatcher;
import com.rainett.javagram.action.annotations.BotAction;
import com.rainett.javagram.action.container.ActionCollector;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import com.rainett.javagram.exceptions.ActionNotFoundException;
import com.rainett.javagram.exceptions.UnknownUpdateTypeException;
import java.lang.reflect.Method;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ActionContainerImpl.
 */
class ActionContainerImplTest {

    @BotAction
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestBotAnnotationA {
    }

    public record DummyAction(int id) implements Action {
        @Override
        public void run(Update update) {
            // No-op for testing
        }
    }

    @Test
    @SneakyThrows
    void testFindByUpdateSuccess() {
        DummyAction expectedAction = new DummyAction(1);

        ActionCollector actionCollector = mock(ActionCollector.class);
        Map<Class<? extends Annotation>, List<Action>> actionsMap = Map.of(
                TestBotAnnotationA.class, List.of(expectedAction)
        );
        when(actionCollector.collectActions()).thenReturn(actionsMap);

        @SuppressWarnings("unchecked")
        UpdateTypeResolver<TestBotAnnotationA> updateTypeResolver =
                (UpdateTypeResolver<TestBotAnnotationA>) mock(UpdateTypeResolver.class);
        when(updateTypeResolver.test(any(Update.class))).thenReturn(true);
        when(updateTypeResolver.getAnnotationType()).thenReturn(TestBotAnnotationA.class);

        @SuppressWarnings("unchecked")
        ActionUpdateMatcher<TestBotAnnotationA> actionUpdateMatcher =
                (ActionUpdateMatcher<TestBotAnnotationA>) mock(ActionUpdateMatcher.class);
        when(actionUpdateMatcher.getAnnotationType()).thenReturn(TestBotAnnotationA.class);
        when(actionUpdateMatcher.test(any(Action.class), any(Update.class))).thenReturn(true);

        ActionContainerImpl container = new ActionContainerImpl(
                actionCollector,
                List.of(updateTypeResolver),
                List.of(actionUpdateMatcher)
        );
        invokeInit(container);
        Update update = new Update();
        Action result = container.findByUpdate(update);
        assertEquals(expectedAction, result);
    }

    @Test
    @SneakyThrows
    void testFindByUpdateNoMatchingUpdateType() {
        ActionCollector actionCollector = mock(ActionCollector.class);
        when(actionCollector.collectActions()).thenReturn(Map.of());

        @SuppressWarnings("unchecked")
        UpdateTypeResolver<TestBotAnnotationA> updateTypeResolver =
                (UpdateTypeResolver<TestBotAnnotationA>) mock(UpdateTypeResolver.class);
        when(updateTypeResolver.test(any(Update.class))).thenReturn(false);

        ActionContainerImpl container = new ActionContainerImpl(
                actionCollector,
                List.of(updateTypeResolver),
                List.of()
        );
        invokeInit(container);
        Update update = new Update();
        Exception exception = assertThrows(UnknownUpdateTypeException.class,
                () -> container.findByUpdate(update));
        assertTrue(exception.getMessage().contains("Unknown update type"));
    }

    @Test
    @SneakyThrows
    void testFindByUpdateNoActionsForResolvedType() {
        ActionCollector actionCollector = mock(ActionCollector.class);
        when(actionCollector.collectActions()).thenReturn(Map.of(
                TestBotAnnotationA.class, List.of()
        ));

        @SuppressWarnings("unchecked")
        UpdateTypeResolver<TestBotAnnotationA> updateTypeResolver =
                (UpdateTypeResolver<TestBotAnnotationA>) mock(UpdateTypeResolver.class);
        when(updateTypeResolver.test(any(Update.class))).thenReturn(true);
        when(updateTypeResolver.getAnnotationType()).thenReturn(TestBotAnnotationA.class);

        @SuppressWarnings("unchecked")
        ActionUpdateMatcher<TestBotAnnotationA> actionUpdateMatcher =
                (ActionUpdateMatcher<TestBotAnnotationA>) mock(ActionUpdateMatcher.class);
        when(actionUpdateMatcher.getAnnotationType()).thenReturn(TestBotAnnotationA.class);
        when(actionUpdateMatcher.test(any(Action.class), any(Update.class))).thenReturn(true);

        ActionContainerImpl container = new ActionContainerImpl(
                actionCollector,
                List.of(updateTypeResolver),
                List.of(actionUpdateMatcher)
        );
        invokeInit(container);
        Update update = new Update();
        Exception exception =
                assertThrows(ActionNotFoundException.class, () -> container.findByUpdate(update));
        assertTrue(exception.getMessage().contains("No actions available for update"));
    }

    @Test
    @SneakyThrows
    void testFindByUpdateNoMatcherForResolvedType() {
        DummyAction action = new DummyAction(1);
        ActionCollector actionCollector = mock(ActionCollector.class);
        when(actionCollector.collectActions()).thenReturn(Map.of(
                TestBotAnnotationA.class, List.of(action)
        ));

        @SuppressWarnings("unchecked")
        UpdateTypeResolver<TestBotAnnotationA> updateTypeResolver =
                (UpdateTypeResolver<TestBotAnnotationA>) mock(UpdateTypeResolver.class);
        when(updateTypeResolver.test(any(Update.class))).thenReturn(true);
        when(updateTypeResolver.getAnnotationType()).thenReturn(TestBotAnnotationA.class);

        ActionContainerImpl container = new ActionContainerImpl(
                actionCollector,
                List.of(updateTypeResolver),
                List.of()
        );
        invokeInit(container);
        Update update = new Update();
        Exception exception =
                assertThrows(IllegalStateException.class, () -> container.findByUpdate(update));
        assertTrue(exception.getMessage()
                .contains("No action matcher registered for annotation type"));
    }

    private static void invokeInit(ActionContainerImpl container) throws Exception {
        Method init = ActionContainerImpl.class.getDeclaredMethod("init");
        init.setAccessible(true);
        init.invoke(container);
    }
}
