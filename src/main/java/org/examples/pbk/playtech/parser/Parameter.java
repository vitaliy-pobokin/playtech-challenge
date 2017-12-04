package org.examples.pbk.playtech.parser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface Parameter {
    String name();
    String description() default "";

    Class converterClass();
}
