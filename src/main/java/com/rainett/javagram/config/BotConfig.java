package com.rainett.javagram.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Telegram Bot.
 * <p>
 * This class binds properties with the prefix "bot" from the application configuration (either
 * application.properties or application.yml). It requires the following properties:
 * </p>
 * <ul>
 *   <li><b>bot.path:</b> The bot webhook path (for example, a ngrok URL).</li>
 *   <li><b>bot.username:</b> The bot username, including the "@" symbol.</li>
 *   <li><b>bot.token:</b> The bot token provided by BotFather in Telegram.</li>
 * </ul>
 */
@Data
@ConfigurationProperties(prefix = "bot")
public class BotConfig {
    /**
     * Bot webhook path. For example, if you are using ngrok, place the ngrok redirect URL here.
     */
    private String path;

    /**
     * Bot username. The "@" symbol should be included.
     */
    private String username;

    /**
     * Bot token. Copy it from BotFather on Telegram.
     */
    private String token;
}
