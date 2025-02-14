package com.rainett.javagram.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.javagram.config.BotConfig;
import com.rainett.javagram.update.service.UpdateService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.ObjectProvider;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

class LongPollingBotTest {

    @Mock
    private BotConfig botConfig;

    @Mock
    private ObjectProvider<UpdateService> serviceProvider;

    @Mock
    private UpdateService updateService;

    private LongPollingBot longPollingBot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(botConfig.getToken()).thenReturn("test-token");
        when(botConfig.getUsername()).thenReturn("@TestBot");
        when(serviceProvider.getIfAvailable()).thenReturn(updateService);

        longPollingBot = new LongPollingBot(botConfig, serviceProvider);
    }

    @Test
    void testGetBotUsername() {
        assertEquals("@TestBot", longPollingBot.getBotUsername());
    }

    @Test
    void testOnUpdateReceived_Success() {
        Update update = mock(Update.class);
        doNothing().when(updateService).handleUpdate(update);

        longPollingBot.onUpdateReceived(update);

        verify(updateService, times(1)).handleUpdate(update);
    }

    @Test
    void testOnUpdateReceived_NoUpdateService() {
        when(serviceProvider.getIfAvailable()).thenReturn(null);
        Update update = mock(Update.class);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            longPollingBot.onUpdateReceived(update);
        });

        assertEquals("UpdateService is not available", exception.getMessage());
    }

    /**
     * Test that the init() method (called after construction) successfully registers the bot.
     * We use Mockito's constructor mocking to intercept the creation of TelegramBotsApi.
     */
    @Test
    void testInit_Success() throws Exception {
        BotSession dummySession = mock(BotSession.class);

        try (MockedConstruction<TelegramBotsApi> mocked =
                     mockConstruction(TelegramBotsApi.class,
                             (mock, context) ->
                                     when(mock.registerBot(longPollingBot)).thenReturn(
                                             dummySession))) {

            Method initMethod = LongPollingBot.class.getDeclaredMethod("init");
            initMethod.setAccessible(true);
            initMethod.invoke(longPollingBot);

            List<TelegramBotsApi> constructed = mocked.constructed();
            assertEquals(1, constructed.size(),
                    "Expected one TelegramBotsApi instance to be created");
            verify(constructed.get(0), times(1)).registerBot(longPollingBot);
        }
    }

    /**
     * Test that the init() method fails when TelegramBotsApi.registerBot throws an exception.
     */
    @Test
    void testInit_Failure() throws Exception {
        TelegramApiException telegramException = new TelegramApiException("Simulated failure");
        try (MockedConstruction<TelegramBotsApi> mocked =
                     mockConstruction(TelegramBotsApi.class, (mock, context) -> {
                         when(mock.registerBot(longPollingBot)).thenThrow(telegramException);
                     })) {

            Method initMethod = LongPollingBot.class.getDeclaredMethod("init");
            initMethod.setAccessible(true);

            InvocationTargetException invocationEx =
                    assertThrows(InvocationTargetException.class, () ->
                            initMethod.invoke(longPollingBot)
                    );

            Throwable cause = invocationEx.getCause();
            assertInstanceOf(IllegalStateException.class, cause,
                    "Expected cause to be IllegalStateException");
            assertTrue(cause.getMessage().contains("Failed to register bot with Telegram Bots API"),
                    "Expected error message to indicate registration failure");
        }
    }
}
