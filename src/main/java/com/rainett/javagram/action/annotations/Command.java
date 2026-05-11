package com.rainett.javagram.action.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an {@link com.rainett.javagram.action.Action} class as a
 * handler for a Telegram bot command.
 * <p>
 * The annotated class must also implement
 * {@link com.rainett.javagram.action.Action} and be a Spring bean
 * (the meta-annotation {@link BotAction} includes
 * {@code @Component}).
 * </p>
 *
 * <pre>{@code
 * @Command("/start")
 * public class StartAction implements Action {
 *     public void run(Update update) { ... }
 * }
 * }</pre>
 *
 * <p>
 * When {@link #value()} is empty the action matches
 * <em>all</em> commands. The bot username suffix
 * (e.g. {@code /start@mybot}) is stripped automatically.
 * </p>
 */
@BotAction()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    /**
     * The command to match, including the leading {@code /}
     * (e.g. {@code "/start"}).
     * <p>
     * When empty (default), the action matches
     * <em>all</em> incoming commands.
     * </p>
     *
     * @return the command string, or {@code ""} to match all
     */
    String value() default "";
}
