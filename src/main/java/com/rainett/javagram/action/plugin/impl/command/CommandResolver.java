package com.rainett.javagram.action.plugin.impl.command;

import com.rainett.javagram.action.annotations.Command;
import com.rainett.javagram.action.updatematcher.UpdateTypeResolver;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Is responsible for resolving updates of type {@link Command}.
 * Implements the {@link UpdateTypeResolver} interface.
 */
public class CommandResolver implements UpdateTypeResolver<Command> {

    /**
     * Checks whether the update contains a command. First, it checks if the update has a message.
     * Then it checks if the message is a command.
     * @param update the input argument
     * @return {@code true} if the update contains a command; {@code false} otherwise
     */
    @Override
    public boolean test(Update update) {
        return update.hasMessage() && update.getMessage().isCommand();
    }

    @Override
    public Class<Command> getAnnotationType() {
        return Command.class;
    }
}
