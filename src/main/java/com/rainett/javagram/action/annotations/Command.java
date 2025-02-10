package com.rainett.javagram.action.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class-annotation which processes telegram commands
 */
@BotAction()
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    /**
     * Command name, "/" symbol should be placed too
     *
     * @return command name
     */
    String value();
}
