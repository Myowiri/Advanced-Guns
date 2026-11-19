package meow.cats.advancedguns.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ServerConfig {

    public static ServerConfig INSTANCE;

    public ServerConfig(){
        INSTANCE = this;
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "advancedguns_server.json");

    public static boolean reloadFromInventory = false;
    public static boolean disableCrosshair = false;
    public static boolean disableNameTags = false;

    public static WeaponConfig ak47 = new WeaponConfig(35f, 13f, 0.0085f, 0.99f, 30, 45, 2);
    public static WeaponConfig m14 = new WeaponConfig(42.5f, 14f, 0.0085f, 0.99f, 20, 2, 2);
    public static WeaponConfig m16 = new WeaponConfig(45f, 12f, 0.0085f, 0.99f, 30, 35, 2);
    public static WeaponConfig rpg = new WeaponConfig(4.5f, 23f, 0.008f, 0.995f, 1, 70, 10);
    public static WeaponConfig awm = new WeaponConfig(47f, 23f, 0.0085f, 0.99f, 5, 70, 32);
    public static WeaponConfig kar98k = new WeaponConfig(38f, 18f, 0.0085f, 0.99f, 5, 4, 32);
    public static WeaponConfig glock17 = new WeaponConfig(15f, 10f, 0.0245f, 0.99f, 17, 30, 10);
    public static WeaponConfig desert_eagle = new WeaponConfig(25f, 14f, 0.0245f, 0.99f, 8, 30, 10);
    public static WeaponConfig remington870 = new WeaponConfig(20f, 21f, 0.0245f, 0.99f, 7, 10, 18);

    private Boolean reload_From_Inventory;
    private Boolean disable_crosshair;
    private Boolean disable_nametags;

    private WeaponConfig ak_47_config;
    private WeaponConfig m14_config;
    private WeaponConfig m16_config;
    private WeaponConfig rpg_launcher_config;
    private WeaponConfig awm_sniper_config;
    private WeaponConfig kar98k_config;
    private WeaponConfig glock_17_config;
    private WeaponConfig desert_eagle_config;
    private WeaponConfig remington870_config;

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
                if (parsedConfig.disable_crosshair != null) disableCrosshair = parsedConfig.disable_crosshair;
                if (parsedConfig.reload_From_Inventory != null) reloadFromInventory = parsedConfig.reload_From_Inventory;
                if (parsedConfig.disable_nametags != null) disableNameTags = parsedConfig.disable_nametags;
                if (parsedConfig.ak_47_config != null) ak47 = parsedConfig.ak_47_config;
                if (parsedConfig.m14_config != null) m14 = parsedConfig.m14_config;
                if (parsedConfig.m16_config != null) m16 = parsedConfig.m16_config;
                if (parsedConfig.rpg_launcher_config != null) rpg = parsedConfig.rpg_launcher_config;
                if (parsedConfig.awm_sniper_config != null) awm = parsedConfig.awm_sniper_config;
                if (parsedConfig.kar98k_config != null) kar98k = parsedConfig.kar98k_config;
                if (parsedConfig.glock_17_config != null) glock17 = parsedConfig.glock_17_config;
                if (parsedConfig.desert_eagle_config != null) desert_eagle = parsedConfig.desert_eagle_config;
                if (parsedConfig.remington870_config != null) remington870 = parsedConfig.remington870_config;
            }
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            ServerConfig configToSave = new ServerConfig();
            configToSave.reload_From_Inventory = reloadFromInventory;
            configToSave.disable_crosshair = disableCrosshair;
            configToSave.disable_nametags = disableNameTags;
            configToSave.ak_47_config = ak47;
            configToSave.m14_config = m14;
            configToSave.m16_config = m16;
            configToSave.rpg_launcher_config = rpg;
            configToSave.awm_sniper_config = awm;
            configToSave.kar98k_config = kar98k;
            configToSave.glock_17_config = glock17;
            configToSave.desert_eagle_config = desert_eagle;
            configToSave.remington870_config = remington870;

            GSON.toJson(configToSave, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
