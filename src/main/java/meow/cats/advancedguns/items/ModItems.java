package meow.cats.advancedguns.items;

import meow.cats.Weapons;
import meow.cats.advancedguns.config.Config;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {

    public static final ResourceKey<CreativeModeTab> WEAPONS_GUNS_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "weapons_tab")
    );

    public static final ResourceKey<CreativeModeTab> WEAPONS_AMMO_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "weapons_ammo_tab")
    );

    public static final CreativeModeTab WEAPONS_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            WEAPONS_GUNS_TAB_KEY,
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup." + Weapons.MOD_ID + ".weapons_tab"))
                    .icon(() -> new ItemStack(ModItems.AK47))
                    .build()
    );

    public static final CreativeModeTab WEAPONS_AMMO_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            WEAPONS_AMMO_TAB_KEY,
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                    .title(Component.translatable("itemGroup." + Weapons.MOD_ID + ".ammo"))
                    .icon(() -> new ItemStack(ModItems.AK47_MAGAZINE))
                    .build()
    );

    private static final ResourceKey<Item> BULLET_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "bullet"));
    public static final Item BULLET = Registry.register(BuiltInRegistries.ITEM, BULLET_KEY, new AmmoItem(new Item.Properties().setId(BULLET_KEY).stacksTo(99)));

    private static final ResourceKey<Item> AMMO_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "ammo"));
    public static final Item AMMO = Registry.register(BuiltInRegistries.ITEM, AMMO_KEY, new AmmoItem(new Item.Properties().setId(AMMO_KEY).stacksTo(99)));

    private static final ResourceKey<Item> A762X51_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "762x51"));
    public static final Item A762X51 = Registry.register(BuiltInRegistries.ITEM, A762X51_KEY, new AmmoItem(new Item.Properties().setId(A762X51_KEY).stacksTo(20)));

    private static final ResourceKey<Item> A792x57_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "792x57"));
    public static final Item A792x57 = Registry.register(BuiltInRegistries.ITEM, A792x57_KEY, new AmmoItem(new Item.Properties().setId(A792x57_KEY).stacksTo(5)));

    private static final ResourceKey<Item> PG7V_ROCKET_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "pg7v_rocket"));
    public static final Item PG7V_ROCKET = Registry.register(BuiltInRegistries.ITEM, PG7V_ROCKET_KEY, new AmmoItem(new Item.Properties().setId(PG7V_ROCKET_KEY).stacksTo(2)));

    private static final ResourceKey<Item> AK47_MAGAZINE_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "ak47-mag"));
    public static final Item AK47_MAGAZINE = Registry.register(BuiltInRegistries.ITEM, AK47_MAGAZINE_KEY, new MagazineItem(new Item.Properties().setId(AK47_MAGAZINE_KEY).stacksTo(1), 30));

    private static final ResourceKey<Item> AWM_MAGAZINE_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "awm_mag"));
    public static final Item AWM_MAGAZINE = Registry.register(BuiltInRegistries.ITEM, AWM_MAGAZINE_KEY, new MagazineItem(new Item.Properties().setId(AWM_MAGAZINE_KEY).stacksTo(1), 5));

    private static final ResourceKey<Item> GLOCK_MAG_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "glock_mag"));
    public static final Item GLOCK_MAG = Registry.register(BuiltInRegistries.ITEM, GLOCK_MAG_KEY, new MagazineItem(new Item.Properties().setId(GLOCK_MAG_KEY).stacksTo(1), 17));

    private static final ResourceKey<Item> AK47_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "ak-47"));
    public static final Item AK47 = Registry.register(BuiltInRegistries.ITEM, AK47_KEY,
            new GunItem(new Item.Properties().setId(AK47_KEY).stacksTo(1),
                    () -> Config.ak47, false, AK47_MAGAZINE, ".7,62x39").setAimY(9.6f).forbidAim()
    );

    private static final ResourceKey<Item> M14_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "m14"));
    public static final Item M14 = Registry.register(BuiltInRegistries.ITEM, M14_KEY,
            new GunItem(new Item.Properties().setId(M14_KEY).stacksTo(1),
                    () -> Config.m14, false, A762X51, ".7,62×51").setAimY(9.6f).setAmmo(A762X51).removeMag().forbidAim()
    );

    private static final ResourceKey<Item> RPG_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "rpg"));
    public static final Item RPG = Registry.register(BuiltInRegistries.ITEM, RPG_KEY,
            new GunItem(new Item.Properties().setId(RPG_KEY).stacksTo(1),
                    () -> Config.rpg, false, PG7V_ROCKET, ".rpg_rocket").shouldExplode().setAmmo(PG7V_ROCKET).setBullet(PG7V_ROCKET).removeMag().forbidAim()
    );

    private static final ResourceKey<Item> AWM_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "awm"));
    public static final Item AWM = Registry.register(BuiltInRegistries.ITEM, AWM_KEY,
            new GunItem(new Item.Properties().setId(AWM_KEY).stacksTo(1),
                    () -> Config.awm, true, AWM_MAGAZINE, ".7,62×67")
    );

    private static final ResourceKey<Item> KAR98K_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "kar98k"));
    public static final Item KAR98K = Registry.register(BuiltInRegistries.ITEM, KAR98K_KEY,
            new GunItem(new Item.Properties().setId(KAR98K_KEY).stacksTo(1),
                    () -> Config.kar98k, false, A792x57, ".7,92×57").setAmmo(A792x57).setBullet(A792x57).removeMag().forbidAim()
    );

    private static final ResourceKey<Item> GLOCK_17_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "glock_17"));
    public static final Item GLOCK17 = Registry.register(BuiltInRegistries.ITEM, GLOCK_17_KEY,
            new GunItem(new Item.Properties().setId(GLOCK_17_KEY).stacksTo(1),
                    () -> Config.glock17, false, GLOCK_MAG, ".9×19").forbidAim()
    );

    private ModItems() {
    }

    public static void initialize() {
        ResourceKey<CreativeModeTab> combatTab = ResourceKey.create(
                Registries.CREATIVE_MODE_TAB,
                Identifier.fromNamespaceAndPath("minecraft", "combat")
        );
        net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents.modifyOutputEvent(WEAPONS_GUNS_TAB_KEY).register(output -> {
            output.accept(GLOCK17);
            output.accept(AK47);
            output.accept(M14);
            output.accept(AWM);
            output.accept(KAR98K);
            output.accept(RPG);
        });

        net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents.modifyOutputEvent(WEAPONS_AMMO_TAB_KEY).register(output -> {
            output.accept(BULLET);
            output.accept(AMMO);
            output.accept(A762X51);
            output.accept(A792x57);
            output.accept(PG7V_ROCKET);
            output.accept(GLOCK_MAG);
            output.accept(AK47_MAGAZINE);
            output.accept(AWM_MAGAZINE);
        });
    }
}
