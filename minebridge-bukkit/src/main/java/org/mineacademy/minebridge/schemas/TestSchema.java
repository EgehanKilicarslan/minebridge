package org.mineacademy.minebridge.schemas;

import lombok.Getter;

public class TestSchema extends BaseSchema {

    @Getter
    private String text;

    public TestSchema(String text) {
        super("test");
        this.text = text;
    }
}