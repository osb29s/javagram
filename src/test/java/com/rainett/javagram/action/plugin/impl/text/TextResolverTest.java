package com.rainett.javagram.action.plugin.impl.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rainett.javagram.action.annotations.Text;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

class TextResolverTest {

    private final TextResolver textResolver = new TextResolver();

    @Test
    void returnsTextAnnotationType() {
        assertEquals(Text.class, textResolver.getAnnotationType());
    }

    @Test
    void testMatchesMessageWithText() {
        Update update = new Update();
        Message message = new Message();
        message.setText("Test message");
        update.setMessage(message);
        assertTrue(textResolver.test(update));
    }

    @Test
    void testFailsMessageWithoutText() {
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        assertFalse(textResolver.test(update));
    }

    @Test
    void testFailsUpdateWithoutMessage() {
        Update update = new Update();
        assertFalse(textResolver.test(update));
    }
}
