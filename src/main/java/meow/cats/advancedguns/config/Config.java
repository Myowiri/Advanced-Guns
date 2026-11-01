package meow.cats.advancedguns.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "advancedguns.json");

    public static WeaponConfig ak47 = new WeaponConfig(35.0f, 13.0f, 0.0085f, 0.99f, 30, 45, 2);
    public static WeaponConfig rpg = new WeaponConfig(4.5f, 35.0f, 0.008f, 0.995f, 1, 70, 10);
    public static WeaponConfig awm = new WeaponConfig(47.0f, 30.0f, 0.0085f, 0.99f, 5, 70, 25);
    public static WeaponConfig glock17 = new WeaponConfig(15.0f, 10.0f, 0.0245f, 0.99f, 17, 30, 10);

    private WeaponConfig ak_47;
    private WeaponConfig rpg_launcher;
    private WeaponConfig awm_sniper;
    private WeaponConfig glock_17;

    public static class WeaponConfig {
        public float speed;
        public float damage;
        public float gravity;
        public float resistance;
        public int maxMag;
        public int reloadCooldown;
        public int shotCooldown;

        public WeaponConfig(float speed, float damage, float gravity, float resistance, int maxMag, int reloadCooldown, int shotCooldown) {
            this.speed = speed;
            this.damage = damage;
            this.gravity = gravity;
            this.resistance = resistance;
            this.maxMag = maxMag;
            this.reloadCooldown = reloadCooldown;
            this.shotCooldown = shotCooldown;
        }
        public float getSpeed() { return speed; }
        public float getDamage() { return damage; }
        public float getGravity() { return gravity; }
        public float getResistance() { return resistance; }
        public int getMaxMag() { return maxMag; }
        public int getReloadCooldown() { return reloadCooldown; }
        public int getShotCooldown() { return shotCooldown; }
    }

    public static void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Config parsedConfig = GSON.fromJson(reader, Config.class);
            if (parsedConfig != null) {
                if (parsedConfig.ak_47 != null) ak47 = parsedConfig.ak_47;
                if (parsedConfig.rpg_launcher != null) rpg = parsedConfig.rpg_launcher;
                if (parsedConfig.awm_sniper != null) awm = parsedConfig.awm_sniper;
                if (parsedConfig.glock_17 != null) glock17 = parsedConfig.glock_17;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            Config configToSave = new Config();
            configToSave.ak_47 = ak47;
            configToSave.rpg_launcher = rpg;
            configToSave.awm_sniper = awm;
            configToSave.glock_17 = glock17;

            GSON.toJson(configToSave, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
