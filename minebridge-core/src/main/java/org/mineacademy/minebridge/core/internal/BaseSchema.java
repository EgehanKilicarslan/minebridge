package org.mineacademy.minebridge.core.internal;

import org.mineacademy.fo.platform.Platform;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

public abstract class BaseSchema {

    private static final Gson GSON = new Gson();
    private static final String SERVER_NAME = initServerName();

    @Getter
    @SerializedName("action")
    private final String action;

    @Getter
    @SerializedName("server")
    private final String server;

    public BaseSchema(String action, String server) {
        this.action = action;
        this.server = server;
    }

    public BaseSchema(String action) {
        this.action = action;
        this.server = SERVER_NAME;
    }

    public BaseSchema(String action, boolean includeServer) {
        this.action = action;
        this.server = includeServer ? SERVER_NAME : null;
    }

    public String toJson() {
        return GSON.toJson(this);
    }

    public static <T extends BaseSchema> T fromJson(String json, Class<T> schemaClass) {
        return GSON.fromJson(json, schemaClass);
    }

    private static String initServerName() {
        Platform.Type type = Platform.getType();
        return type == Platform.Type.BUKKIT ? Platform.getCustomServerName() : type.toString().toLowerCase();
    }
}
