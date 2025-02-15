package com.rainett.javagram.action.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-annotation which processes telegram commands
 */
@BotAction()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    /**
     * Command name, "/" symbol should be placed too
     *
     * @return command name
     */
    String value() default "";
}
