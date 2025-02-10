package com.rainett.javagram.config;

import com.rainett.javagram.bot.LongPollingBot;
import com.rainett.javagram.bot.WebhookBot;
import com.rainett.javagram.update.service.UpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Autoconfiguration class for initializing Telegram bot instances based on the provided BotConfig.
 * <p>
 * This configuration creates either a {@link WebhookBot} or a {@link LongPollingBot}
 * depending on the presence of the {@code bot.path} property in the configuration.
 * </p>
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.rainett.javagram")
@EnableConfigurationProperties(BotConfig.class)
public class AutoConfig {

    /**
     * Creates a {@link WebhookBot} bean if the {@code bot.path} property is defined.
     * <p>
     * When {@code bot.path} is provided, a webhook is assumed to be enabled and the
     * {@link WebhookBot} is instantiated using a {@link SetWebhook} instance with the
     * given webhook URL.
     * </p>
     *
     * @param botConfig the bot configuration properties
     * @return a {@link DefaultAbsSender} configured as a {@link WebhookBot}
     * @throws TelegramApiException if there is an error initializing the webhook configuration
     */
    @Bean
    @ConditionalOnProperty(name = "bot.path")
    public DefaultAbsSender webhookBot(BotConfig botConfig) throws TelegramApiException {
        log.info("Webhook enabled (bot.path: {}), creating WebhookBot instance",
                botConfig.getPath());
        return new WebhookBot(new SetWebhook(botConfig.getPath()), botConfig);
    }

    /**
     * Creates a {@link LongPollingBot} bean as a fallback when no {@link DefaultAbsSender}
     * bean is defined.
     * <p>
     * This bean is only created when the webhook configuration is absent (i.e., no bean of type
     * {@link DefaultAbsSender} exists), ensuring that a LongPollingBot is instantiated to handle
     * updates.
     * </p>
     *
     * @param botConfig     the bot configuration properties
     * @param updateService an optional provider for the {@link UpdateService} to process updates
     * @return a {@link DefaultAbsSender} configured as a {@link LongPollingBot}
     */
    @Bean
    @ConditionalOnMissingBean(DefaultAbsSender.class)
    public DefaultAbsSender longPollingBot(BotConfig botConfig,
                                           ObjectProvider<UpdateService> updateService) {
        log.info("Webhook disabled, creating LongPollingBot instance");
        return new LongPollingBot(botConfig, updateService);
    }
}
