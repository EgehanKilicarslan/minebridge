package org.mineacademy.minebridge.websocket;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.mineacademy.minebridge.annotation.WebSocketAction;
import org.mineacademy.minebridge.implementations.WebSocketAware;
import org.mineacademy.minebridge.schemas.BaseSchema;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WebSocketActionHandler {

    private final Map<String, ActionMethod> actionMethods = new HashMap<>();
    private static final Gson gson = new Gson();
    private Client client; // Add reference to the client

    // Add setter for client
    public void setClient(Client client) {
        this.client = client;
    }

    // Add getter for client
    public Client getClient() {
        return client;
    }

    public void registerClass(Object instance) {
        Class<?> clazz = instance.getClass();

        for (Method method : clazz.getMethods()) {
            WebSocketAction annotation = method.getAnnotation(WebSocketAction.class);

            if (annotation != null) {
                String action = annotation.value();
                Class<? extends BaseSchema> schemaClass = annotation.schema();

                actionMethods.put(action, new ActionMethod(instance, method, schemaClass));

                // If instance is WebSocketAware, pass client reference
                if (instance instanceof WebSocketAware) {
                    ((WebSocketAware) instance).setClient(client);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public boolean handleMessage(String message) {
        try {
            JsonElement jsonElement = new JsonParser().parse(message);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement actionElement = jsonObject.get("action");

            if (actionElement == null || !actionElement.isJsonPrimitive()) {
                return false;
            }

            String action = actionElement.getAsString();
            ActionMethod actionMethod = actionMethods.get(action);

            if (actionMethod == null) {
                return false;
            }

            if (actionMethod.schemaClass != BaseSchema.class) {
                Object schema = gson.fromJson(jsonObject, actionMethod.schemaClass);
                actionMethod.method.invoke(actionMethod.instance, schema);
            } else {
                actionMethod.method.invoke(actionMethod.instance, message);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class ActionMethod {
        private final Object instance;
        private final Method method;
        private final Class<? extends BaseSchema> schemaClass;

        public ActionMethod(Object instance, Method method, Class<? extends BaseSchema> schemaClass) {
            this.instance = instance;
            this.method = method;
            this.schemaClass = schemaClass;
        }
    }
}