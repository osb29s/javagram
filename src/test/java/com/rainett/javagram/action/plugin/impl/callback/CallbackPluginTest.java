package com.rainett.javagram.action.plugin.impl.callback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rainett.javagram.action.actionmatcher.ActionUpdateMatcher;
import com.rainett.javagram.action.annotations.Callback;
import com.rainett.javagram.action.comparator.AnnotationComparator;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import org.junit.jupiter.api.Test;

class CallbackPluginTest {
    private final CallbackPlugin callbackPlugin = new CallbackPlugin();

    @Test
    void testGetAnnotationType() {
        assertEquals(Callback.class, callbackPlugin.getAnnotationType());
    }

    @Test
    void testGetUpdateTypeMatcher() {
        UpdateTypeResolver<Callback> resolver = callbackPlugin.getUpdateTypeMatcher();
        assertNotNull(resolver);
    }

    @Test
    void testGetActionMatcher() {
        ActionUpdateMatcher<Callback> matcher = callbackPlugin.getActionMatcher();
        assertNotNull(matcher);
    }

    @Test
    void testGetAnnotationComparator() {
        AnnotationComparator<Callback> comparator = callbackPlugin.getAnnotationComparator();
        assertNotNull(comparator);
    }
}
