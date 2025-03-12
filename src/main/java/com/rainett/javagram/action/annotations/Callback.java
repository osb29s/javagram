package com.rainett.javagram.action.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-annotation which marks class as the one that processes callbacks
 */
@BotAction()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Callback {
    /**
     * Callback name
     *
     * @return callback name
     */
    String value() default "";
}
