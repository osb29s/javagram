package com.rainett.javagram.action.plugin.impl.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rainett.javagram.action.actionmatcher.ActionUpdateMatcher;
import com.rainett.javagram.action.annotations.Command;
import com.rainett.javagram.action.comparator.AnnotationComparator;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import com.rainett.javagram.config.BotConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CommandPluginTest {
    private CommandPlugin commandPlugin;

    @Mock
    private BotConfig botConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commandPlugin = new CommandPlugin(botConfig);
    }

    @Test
    void testGetAnnotationType() {
        assertEquals(Command.class, commandPlugin.getAnnotationType());
    }

    @Test
    void testGetUpdateTypeMatcher() {
        UpdateTypeResolver<Command> resolver = commandPlugin.getUpdateTypeMatcher();
        assertNotNull(resolver);
    }

    @Test
    void testGetActionMatcher() {
        ActionUpdateMatcher<Command> matcher = commandPlugin.getActionMatcher();
        assertNotNull(matcher);
    }

    @Test
    void testGetAnnotationComparator() {
        AnnotationComparator<Command> comparator = commandPlugin.getAnnotationComparator();
        assertNotNull(comparator);
    }
}
