package com.rainett.javagram.action.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-annotation which processes text messages
 */
@BotAction()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Text {
    /**
     * Specifies the exact text, that will trigger the action on an update
     * @return exact text
     */
    String equals() default "";

    /**
     * Specifies the substring, that will trigger the action on an update, if it is contained
     * by an update
     * @return substring
     */
    String contains() default "";

    /**
     * Specifies the prefix, that will trigger the action on an update
     * @return prefix
     */
    String startsWith() default "";

    /**
     * Specifies the suffix, that will trigger the action on an update
     * @return suffix
     */
    String endsWith() default "";

    /**
     * Specifies the regular expression, that will trigger the action on an update
     * @return regular expression
     */
    String regex() default "";
}
