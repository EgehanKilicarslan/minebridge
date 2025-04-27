package org.mineacademy.minebridge.core.schema;

import org.mineacademy.minebridge.core.internal.BaseSchema;

import lombok.Getter;

public class TestSchema extends BaseSchema {

    @Getter
    private final String text;

    public TestSchema(String text) {
        super("test");
        this.text = text;
    }
}