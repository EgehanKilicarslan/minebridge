package org.mineacademy.minebridge.bungee.schemas;

import org.mineacademy.minebridge.schemas.BaseSchema;

import lombok.Getter;

public class TestSchema extends BaseSchema {

    @Getter
    private String text;

    public TestSchema(String text) {
        super("test", "bungeecord");
        this.text = text;
    }
}