package com.rainett.javagram.bot;

import com.rainett.javagram.config.BotConfig;
import com.rainett.javagram.update.service.UpdateService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * LongPollingBot is a Telegram bot implementation that uses long polling to receive updates.
 * <p>
 * It automatically registers itself with the Telegram Bots API upon initialization and delegates
 * update processing to an injected {@link UpdateService}.
 * </p>
 */
@Slf4j
public class LongPollingBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final ObjectProvider<UpdateService> serviceProvider;

    /**
     * Constructs a LongPollingBot with the specified configuration and update service provider.
     *
     * @param botConfig the bot configuration containing credentials and settings.
     * @param service   the provider for the {@link UpdateService} that handles incoming updates.
     */
    public LongPollingBot(BotConfig botConfig, ObjectProvider<UpdateService> service) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        this.serviceProvider = service;
    }

    /**
     * Processes an incoming update from Telegram.
     * <p>
     * The update is passed to the available {@link UpdateService} for further processing.
     * If no {@code UpdateService} is available, an {@link IllegalStateException} is thrown.
     * </p>
     *
     * @param update the update received from Telegram.
     */
    @Override
    public void onUpdateReceived(Update update) {
        log.info("Received update: {}", update);
        UpdateService updateService = serviceProvider.getIfAvailable();
        if (updateService == null) {
            String errorMessage = "UpdateService is not available";
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        updateService.handleUpdate(update);
    }

    /**
     * Returns the bot's username as defined in the configuration.
     *
     * @return the bot username.
     */
    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    /**
     * Registers the bot with the Telegram Bots API after construction.
     * <p>
     * This method is automatically invoked by Spring after bean construction. It attempts to
     * register the bot with a {@link DefaultBotSession} and logs the result.
     * </p>
     *
     * @throws IllegalStateException if registration with the Telegram API fails.
     */
    @PostConstruct
    private void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            log.info("LongPollingBot registered successfully with username: {}", getBotUsername());
        } catch (TelegramApiException e) {
            String errorMessage = "Failed to register bot with Telegram Bots API";
            log.error(errorMessage, e);
            throw new IllegalStateException(errorMessage, e);
        }
    }
}
