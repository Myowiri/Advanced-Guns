package meow.cats.advancedguns.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("advancedguns_client");


    public boolean hud = false;
    public boolean Crosshair = true;



    public static ClientConfig INSTANCE = new ClientConfig();

    public static void load() {
        try {
            File file = PATH.toFile();

            if (file.exists()) {
                INSTANCE = GSON.fromJson(new FileReader(file), ClientConfig.class);
                if (INSTANCE == null) {
                    INSTANCE = new ClientConfig();
                }
            } else {
                INSTANCE = new ClientConfig();
                save();
                return;
            }

            save();
        } catch (Exception e) {
            e.printStackTrace();
            INSTANCE = new ClientConfig();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());

            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
