package com.rainett.javagram.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Executable
@Retention(RetentionPolicy.RUNTIME)
public @interface Callback {
    String value();
    boolean fromSender() default false;
}
