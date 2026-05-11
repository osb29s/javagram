package com.rainett.javagram.action.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.stereotype.Component;

/**
 * Meta-annotation applied to all javagram action annotations
 * ({@code @Command}, {@code @Text}, {@code @Callback}).
 * <p>
 * Including {@code @Component} ensures that any class annotated with
 * a {@code @BotAction}-meta-annotated annotation is auto-detected as
 * a Spring bean without requiring an explicit {@code @Component}
 * declaration on the action class itself.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface BotAction {
}
