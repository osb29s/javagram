package com.rainett.javagram.action.plugin.impl.command;

import com.rainett.javagram.action.actionmatcher.ActionUpdateMatcher;
import com.rainett.javagram.action.annotations.Command;
import com.rainett.javagram.action.comparator.AnnotationComparator;
import com.rainett.javagram.action.plugin.UpdateTypePlugin;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import com.rainett.javagram.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Plugin of all infrastructure components for handling {@link Command} annotations.
 * Implements the {@link UpdateTypePlugin} interface.
 */
@Component
@RequiredArgsConstructor
public class CommandPlugin implements UpdateTypePlugin<Command> {
    private final BotConfig botConfig;

    /**
     * The annotation type supported by this plugin.
     * @return the annotation type
     */
    @Override
    public Class<Command> getAnnotationType() {
        return Command.class;
    }

    /**
     * Update type resolver for {@link Command} annotations.
     * @return the update type resolver
     */
    @Override
    @Bean("commandResolver")
    public UpdateTypeResolver<Command> getUpdateTypeMatcher() {
        return new CommandResolver();
    }

    /**
     * Action update matcher for {@link Command} annotations.
     * @return the action update matcher
     */
    @Override
    @Bean("commandMatcher")
    public ActionUpdateMatcher<Command> getActionMatcher() {
        return new CommandUpdateMatcher(botConfig);
    }

    /**
     * Annotation comparator for {@link Command} annotations.
     * @return the annotation comparator
     */
    @Override
    @Bean("commandComparator")
    public AnnotationComparator<Command> getAnnotationComparator() {
        return new CommandComparator();
    }
}
