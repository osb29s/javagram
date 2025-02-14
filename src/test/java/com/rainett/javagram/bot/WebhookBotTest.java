package com.rainett.javagram.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.rainett.javagram.config.BotConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

class WebhookBotTest {
    @Mock
    private BotConfig botConfig;

    @Mock
    private SetWebhook setWebhook;

    private WebhookBot webhookBot;

    @BeforeEach
    void setUp() throws TelegramApiException {
        MockitoAnnotations.openMocks(this);
        when(botConfig.getToken()).thenReturn("test-token");
        when(botConfig.getPath()).thenReturn("https://example.com/webhook");
        when(botConfig.getUsername()).thenReturn("@TestBot");

        webhookBot = new WebhookBot(setWebhook, botConfig);
    }

    @Test
    void testGetBotPath() {
        assertEquals("https://example.com/webhook", webhookBot.getBotPath());
    }

    @Test
    void testGetBotUsername() {
        assertEquals("@TestBot", webhookBot.getBotUsername());
    }

    @Test
    void testOnWebhookUpdateReceived() {
        Update update = mock(Update.class);
        assertNull(webhookBot.onWebhookUpdateReceived(update));
    }
}
