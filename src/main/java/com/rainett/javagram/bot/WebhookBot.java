package com.rainett.javagram.bot;

import com.rainett.javagram.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

/**
 * WebhookBot is a Telegram bot implementation that utilizes webhooks.
 * <p>
 * In this application, all Telegram updates are received and processed by a dedicated controller.
 * Consequently, the {@code onWebhookUpdateReceived} method is not used to handle updates.
 * It is implemented solely to fulfill the contract of {@link SpringWebhookBot}.
 * </p>
 */
@Slf4j
public class WebhookBot extends SpringWebhookBot {
    private final BotConfig botConfig;

    /**
     * Constructs a new WebhookBot with the specified webhook settings and bot configuration.
     *
     * @param setWebhook the webhook configuration for Telegram
     * @param botConfig  the bot configuration properties
     * @throws TelegramApiException if an error occurs while executing the webhook configuration
     */
    public WebhookBot(SetWebhook setWebhook, BotConfig botConfig) throws TelegramApiException {
        super(setWebhook, botConfig.getToken());
        this.botConfig = botConfig;
        execute(setWebhook);
        log.info("WebhookBot initialized with webhook URL: {}", botConfig.getPath());
    }

    /**
     * This method is not used because update handling is performed by an external controller.
     * It is implemented only to fulfill the contract defined by {@link SpringWebhookBot}.
     *
     * @param update the incoming update from Telegram
     * @return always returns null as updates are processed externally
     */
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        log.debug("onWebhookUpdateReceived was called but is not used "
                  + "in this configuration. Update: {}", update);
        return null;
    }

    /**
     * Returns the webhook path configured for this bot.
     *
     * @return the bot webhook path.
     */
    @Override
    public String getBotPath() {
        return botConfig.getPath();
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
}
