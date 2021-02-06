package tech.dttp.serversimplified;

import com.google.gson.*;
import tech.dttp.serversimplified.commands.MuteCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Configuration {

    public static final String PREFFERED_FILENAME = "config/serversimplified.json";
    private static Logger LOG = LogManager.getLogger();
    private final Path origin;
    Permissions permissions;

    public Configuration(Path origin) {
        this(origin, new JsonArray());
    }

    public Configuration(Path origin, JsonArray permission) {
        this.origin = origin;
        permissions = Permissions.loadFromJson(permission);
    }

    /**
     * Load a configuration file
     * @param path  The path to read from
     * @return      The constructed configuration object
     */
    public static Configuration loadFromPath(Path path) {
        try {
            String jsonString = new String(Files.readAllBytes(path));
            JsonObject object = new JsonParser().parse(jsonString).getAsJsonObject();
            JsonArray array = object.getAsJsonArray("permissions");
            JsonObject muted = object.getAsJsonObject("muted");
            JsonObject settings = object.getAsJsonObject("settings");
            ServerSimplified.settings = new Settings(settings);
            MuteCommand.fromJson(muted);
            return new Configuration(path, array);
        } catch (Exception e) {
            LOG.info("Failed to read JSON");
            return new Configuration(path);
        }
    }

    /**
     * @return {@link #loadFromPath(Path)} with the default configuration filename
     */
    public static Configuration load() {
        return loadFromPath(Paths.get(PREFFERED_FILENAME));
    }

    public void save() throws IOException {
        JsonObject object = new JsonObject();

        object.add("permissions", getPermissions().toJson());
        object.add("muted", MuteCommand.toJson());
        object.add("settings", ServerSimplified.settings.toJson());
        

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.write(origin, gson.toJson(object).getBytes());
    }

    public Permissions getPermissions() {
        return permissions;
    }
}
