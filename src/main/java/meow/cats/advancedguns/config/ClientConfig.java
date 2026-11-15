package meow.cats.advancedguns.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("advancedguns_client.json");


    public boolean hud = false;
    public boolean ammoCounter = true;
    public boolean magIndicator = true;
    public boolean coolDown = true;


    public enum CrosshairStyle {Default, Custom, None}
    public CrosshairStyle crosshairStyle = CrosshairStyle.Default;
    public int crosshairColor = 0xFFFFFFFF;
    public boolean centerDot = true;
    public boolean lines = true;
    public int width = 1;
    public int padding = 1;
    public float scale = 1f;



    public static ClientConfig INSTANCE = new ClientConfig();

    public Color getCrosshairColor() { return new Color(crosshairColor, true); }
    public void setCrosshairColor(Color color) { this.crosshairColor = color.getRGB(); }

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                try (Reader reader = Files.newBufferedReader(PATH)) {
                    INSTANCE = GSON.fromJson(reader, ClientConfig.class);
                }
                if (INSTANCE == null) {
                    INSTANCE = new ClientConfig();
                }
            } else {
                INSTANCE = new ClientConfig();
                save();
                return;
            }

            INSTANCE.validate();
            save();
        } catch (Exception e) {
            e.printStackTrace();
            INSTANCE = new ClientConfig();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            INSTANCE.validate();

            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validate() {
        scale = clamp(scale, .7f, 3f);
        padding = clamp(padding, 0, 10);
        width = clamp(width, 0, 10);
    }



    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
