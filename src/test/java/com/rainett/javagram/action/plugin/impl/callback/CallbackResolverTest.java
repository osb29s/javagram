package com.rainett.javagram.action.plugin.impl.callback;

import static org.junit.jupiter.api.Assertions.*;

import com.rainett.javagram.action.annotations.Callback;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

class CallbackResolverTest {
    private final CallbackResolver callbackResolver = new CallbackResolver();

    @Test
    void resolvesCallbackUpdates() {
        Update update = new Update();
        assertFalse(callbackResolver.test(update));
        update.setCallbackQuery(new CallbackQuery());
        assertTrue(callbackResolver.test(update));
    }

    @Test
    void returnsAnnotationType() {
        assertEquals(Callback.class, callbackResolver.getAnnotationType());
    }
}