package com.rainett.javagram.action.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an {@link com.rainett.javagram.action.Action} class as a
 * handler for inline keyboard callback queries.
 * <p>
 * The annotated class must also implement
 * {@link com.rainett.javagram.action.Action} and be a Spring bean
 * (the meta-annotation {@link BotAction} includes
 * {@code @Component}).
 * </p>
 *
 * <p>
 * Callback data uses {@code :} as a delimiter —
 * {@link #value()} is matched against the part
 * <em>before</em> the first {@code :}. This lets a single action
 * handle callbacks that carry different payloads:
 * </p>
 *
 * <pre>{@code
 * // .button("Delete", "delete", "42") → callback_data = "delete:42"
 * @Callback("delete")
 * public class DeleteAction implements Action {
 *     public void run(Update update) {
 *         String raw = update.getCallbackQuery().getData();
 *         String itemId = raw.split(":")[1]; // "42"
 *     }
 * }
 * }</pre>
 *
 * <p>
 * When {@link #value()} is empty the action matches
 * <em>all</em> callbacks.
 * </p>
 */
@BotAction()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Callback {
    /**
     * The callback key to match — the segment of
     * {@code callback_data} before the first {@code :}.
     * <p>
     * When empty (default), the action matches
     * <em>all</em> callback queries.
     * </p>
     *
     * @return the callback key, or {@code ""} to match all
     */
    String value() default "";
}
