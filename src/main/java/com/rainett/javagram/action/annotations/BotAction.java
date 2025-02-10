package com.rainett.javagram.action.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.stereotype.Component;

/**
 * Base annotation for all Telegram processing actions
 */
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface BotAction {
}
