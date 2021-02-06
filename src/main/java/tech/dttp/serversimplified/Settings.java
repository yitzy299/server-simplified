package tech.dttp.serversimplified;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Settings {
    private boolean muteMessages = true;
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonObject configObject = new JsonObject();
        configObject.addProperty("mute_messages", true);
        object.add("settings", configObject);
        return object;
    }
    public Settings(JsonObject object) {
        this.muteMessages = object.get("mute_messages").getAsBoolean();
    }
    public boolean shouldSendMuteMessages() {
        return this.muteMessages;
    }
}
