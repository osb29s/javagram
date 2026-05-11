package com.rainett.javagram.config;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import com.rainett.javagram.bot.LongPollingBot;
import com.rainett.javagram.bot.WebhookBot;
import com.rainett.javagram.update.service.UpdateService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.beans.factory.ObjectProvider;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

class AutoConfigTest {
    private final AutoConfig autoConfig = new AutoConfig();

    @Test
    void webhookBot_returnsWebhookBotInstance() throws TelegramApiException {
        BotConfig config = mock(BotConfig.class);
        when(config.getPath()).thenReturn("https://example.com/webhook");
        when(config.getToken()).thenReturn("token");
        when(config.getUsername()).thenReturn("@bot");

        try (MockedConstruction<WebhookBot> ignored = mockConstruction(WebhookBot.class)) {
            DefaultAbsSender bot = autoConfig.webhookBot(config);
            assertInstanceOf(WebhookBot.class, bot);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void longPollingBot_returnsLongPollingBotInstance() {
        BotConfig config = mock(BotConfig.class);
        when(config.getToken()).thenReturn("token");
        when(config.getUsername()).thenReturn("@bot");
        ObjectProvider<UpdateService> provider = mock(ObjectProvider.class);

        try (MockedConstruction<LongPollingBot> ignored = mockConstruction(LongPollingBot.class)) {
            DefaultAbsSender bot = autoConfig.longPollingBot(config, provider);
            assertInstanceOf(LongPollingBot.class, bot);
        }
    }
}
