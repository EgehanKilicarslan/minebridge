package org.mineacademy.minebridge.schema;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

public abstract class BaseSchema {

    @Getter
    @SerializedName("action")
    private final String action;

    private static final Gson gson = new Gson();

    public BaseSchema(String action) {
        this.action = action;
    }

    public String toJson() {
        try {
            return gson.toJson(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert schema to JSON", e);
        }
    }
}
