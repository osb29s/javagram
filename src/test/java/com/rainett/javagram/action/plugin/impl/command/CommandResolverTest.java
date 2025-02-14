package com.rainett.javagram.action.plugin.impl.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rainett.javagram.action.annotations.Command;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

class CommandResolverTest {
    private final CommandResolver commandResolver = new CommandResolver();

    @Test
    void returnsAnnotationType() {
        assertEquals(Command.class, commandResolver.getAnnotationType());
    }

    @Test
    void testMatchesCommand() {
        Update update = new Update();
        Message message = new Message();
        MessageEntity command = new MessageEntity("bot_command", 0, 5);
        message.setEntities(List.of(command));
        message.setText("/start");
        update.setMessage(message);
        assertTrue(commandResolver.test(update));
    }

    @Test
    void testFailsMessageWithoutCommand() {
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        assertFalse(commandResolver.test(update));
    }

    @Test
    void testFailsUpdateWithoutCommand() {
        Update update = new Update();
        assertFalse(commandResolver.test(update));
    }
}
