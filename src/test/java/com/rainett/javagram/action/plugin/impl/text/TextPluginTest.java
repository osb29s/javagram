package com.rainett.javagram.action.plugin.impl.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rainett.javagram.action.actionmatcher.ActionUpdateMatcher;
import com.rainett.javagram.action.annotations.Text;
import com.rainett.javagram.action.comparator.AnnotationComparator;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TextPluginTest {
    private TextPlugin textPlugin;

    @BeforeEach
    void setUp() {
        textPlugin = new TextPlugin();
    }

    @Test
    void testGetAnnotationType() {
        assertEquals(Text.class, textPlugin.getAnnotationType());
    }

    @Test
    void testGetUpdateTypeMatcher() {
        UpdateTypeResolver<Text> resolver = textPlugin.getUpdateTypeMatcher();
        assertNotNull(resolver);
    }

    @Test
    void testGetActionMatcher() {
        ActionUpdateMatcher<Text> matcher = textPlugin.getActionMatcher();
        assertNotNull(matcher);
    }

    @Test
    void testGetAnnotationComparator() {
        AnnotationComparator<Text> comparator = textPlugin.getAnnotationComparator();
        assertNotNull(comparator);
    }
}
