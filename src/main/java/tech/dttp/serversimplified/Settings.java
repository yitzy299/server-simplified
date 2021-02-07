package tech.dttp.serversimplified;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import tech.dttp.serversimplified.commands.MaintenanceModeCommand;

public class Settings {
    private static boolean muteMessages = true;
    public static boolean maintenance = false;
    public static JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("mute_messages", true);
        object.addProperty("maintenance_mode", MaintenanceModeCommand.isMaintenance());
        return object;
    }
    public static void parse(JsonObject object) {
        muteMessages = object.get("mute_messages").getAsBoolean();
        maintenance = object.get("maintenance_mode").getAsBoolean();
        MaintenanceModeCommand.setMaintenanceMode(maintenance);
    }
    public static boolean shouldSendMuteMessages() {
        return muteMessages;
    }
}
