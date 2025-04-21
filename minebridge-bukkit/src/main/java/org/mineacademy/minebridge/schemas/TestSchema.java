package org.mineacademy.minebridge.schemas;

import org.mineacademy.minebridge.MineBridgeBukkit;

import lombok.Getter;

public class TestSchema extends BaseSchema {

    @Getter
    private String text;

    public TestSchema(String text) {
        super("test", MineBridgeBukkit.getServerName());
        this.text = text;
    }
}