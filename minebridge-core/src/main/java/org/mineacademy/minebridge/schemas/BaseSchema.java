package org.mineacademy.minebridge.schemas;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

public abstract class BaseSchema {

    @Getter
    @SerializedName("action")
    private final String action;

    @Getter
    @SerializedName("server")
    private final String server;

    private static final Gson gson = new Gson();

    public BaseSchema(String action, String server) {
        this.action = action;
        this.server = server;
    }

    public String toJson() {
        try {
            return gson.toJson(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert schema to JSON", e);
        }
    }
}
