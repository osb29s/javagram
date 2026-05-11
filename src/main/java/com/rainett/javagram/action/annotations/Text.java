package com.rainett.javagram.action.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an {@link com.rainett.javagram.action.Action} class as a
 * handler for plain text messages.
 * <p>
 * At least one field must be set; all fields default to {@code ""}
 * (ignored when empty). The annotated class must also implement
 * {@link com.rainett.javagram.action.Action} and be a Spring bean
 * (the meta-annotation {@link BotAction} includes
 * {@code @Component}).
 * </p>
 *
 * <pre>{@code
 * @Text(equals = "hello")
 * public class HelloAction implements Action { ... }
 *
 * @Text(regex = "order #\\d+")
 * public class OrderAction implements Action { ... }
 * }</pre>
 *
 * <p><b>Matching priority</b> (highest to lowest):</p>
 * <ol>
 *   <li>{@link #equals()} — exact match</li>
 *   <li>{@link #regex()} — Java regex match</li>
 *   <li>{@link #startsWith()} / {@link #endsWith()}</li>
 *   <li>{@link #contains()} — substring match</li>
 * </ol>
 * Within the same tier, the longer pattern wins.
 */
@BotAction()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Text {
    /**
     * Triggers the action when the message text is exactly this
     * value. Highest priority among all {@code @Text} fields.
     *
     * @return exact text to match, or {@code ""} to ignore
     */
    String equals() default "";

    /**
     * Triggers the action when the message text contains this
     * substring. Lowest priority among all {@code @Text} fields.
     *
     * @return substring to match, or {@code ""} to ignore
     */
    String contains() default "";

    /**
     * Triggers the action when the message text starts with
     * this prefix.
     *
     * @return prefix to match, or {@code ""} to ignore
     */
    String startsWith() default "";

    /**
     * Triggers the action when the message text ends with
     * this suffix.
     *
     * @return suffix to match, or {@code ""} to ignore
     */
    String endsWith() default "";

    /**
     * Triggers the action when the message text matches this
     * Java regular expression. Second-highest priority after
     * {@link #equals()}.
     *
     * @return Java regex pattern, or {@code ""} to ignore
     */
    String regex() default "";
}
