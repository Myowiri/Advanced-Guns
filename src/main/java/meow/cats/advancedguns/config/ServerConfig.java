package meow.cats.advancedguns.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ServerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "advancedguns_server.json");

    public static WeaponConfig ak47 = new WeaponConfig(35f, 13f, 0.0085f, 0.99f, 30, 45, 2);
    public static WeaponConfig m14 = new WeaponConfig(42.5f, 10f, 0.0085f, 0.99f, 20, 2, 2);
    public static WeaponConfig rpg = new WeaponConfig(4.5f, 23f, 0.008f, 0.995f, 1, 70, 10);
    public static WeaponConfig awm = new WeaponConfig(47f, 23f, 0.0085f, 0.99f, 5, 70, 37);
    public static WeaponConfig kar98k = new WeaponConfig(38f, 23f, 0.0085f, 0.99f, 5, 2, 48);
    public static WeaponConfig glock17 = new WeaponConfig(15f, 10f, 0.0245f, 0.99f, 17, 30, 10);

    private WeaponConfig ak_47_config;
    private WeaponConfig m14_config;
    private WeaponConfig rpg_launcher_config;
    private WeaponConfig awm_sniper_config;
    private WeaponConfig kar98k_config;
    private WeaponConfig glock_17_config;

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
            ServerConfig parsedConfig = GSON.fromJson(reader, ServerConfig.class);
            if (parsedConfig != null) {
                if (parsedConfig.ak_47_config != null) ak47 = parsedConfig.ak_47_config;
                if (parsedConfig.m14_config != null) m14 = parsedConfig.m14_config;
                if (parsedConfig.rpg_launcher_config != null) rpg = parsedConfig.rpg_launcher_config;
                if (parsedConfig.awm_sniper_config != null) awm = parsedConfig.awm_sniper_config;
                if (parsedConfig.kar98k_config != null) kar98k = parsedConfig.kar98k_config;
                if (parsedConfig.glock_17_config != null) glock17 = parsedConfig.glock_17_config;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            ServerConfig configToSave = new ServerConfig();
            configToSave.ak_47_config = ak47;
            configToSave.m14_config = m14;
            configToSave.rpg_launcher_config = rpg;
            configToSave.awm_sniper_config = awm;
            configToSave.kar98k_config = kar98k;
            configToSave.glock_17_config = glock17;

            GSON.toJson(configToSave, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
