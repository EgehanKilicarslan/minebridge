package org.mineacademy.minebridge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mineacademy.minebridge.schemas.BaseSchema;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WebSocketAction {
    String value(); // The action name

    Class<? extends BaseSchema> schema() default BaseSchema.class;
}
