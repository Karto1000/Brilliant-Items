package brilliant_items.internal.config;

import brilliant_items.BrilliantItems;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

public class JsonConfigManager {
    public static final Gson GSON = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocationAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .setPrettyPrinting()
            .create();
    public static final String FILE_NAME = "brilliant_items_bindings.json";
    public static final Validator VALIDATOR;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();
    }

    public static JsonConfig config = new JsonConfig();

    public static void init() {
        File configDir = new File(Minecraft.getMinecraft().gameDir, "config");
        File configFile = new File(configDir, FILE_NAME);

        @Nullable
        JsonConfig config;
        if (!configFile.exists()) config = createConfig(configFile);
        else config = readConfig(configFile);

        if (config == null) {
            BrilliantItems.LOGGER.warn(
                    "Config could not be created / read, any user defined effects will not be registered");
            return;
        }

        JsonConfigManager.config = config;
    }

    @Nullable
    private static JsonConfig createConfig(File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            JsonConfig config = new JsonConfig();
            BrilliantItems.LOGGER.info("Created default configuration file");
            writer.write(GSON.toJson(config));
            return config;
        } catch (IOException e) {
            BrilliantItems.LOGGER.error("Failed to create default config file", e);
            return null;
        }
    }

    @Nullable
    private static JsonConfig readConfig(File configFile) {
        try (FileReader reader = new FileReader(configFile)) {
            JsonConfig config = GSON.fromJson(reader, JsonConfig.class);
            BrilliantItems.LOGGER.info("Successfully read config");
            return config;
        } catch (IOException | JsonSyntaxException e) {
            BrilliantItems.LOGGER.error("Failed to read config file", e);
            return null;
        }
    }
}
