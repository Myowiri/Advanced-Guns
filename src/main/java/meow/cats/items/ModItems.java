package meow.cats.items;

import meow.cats.Weapons;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
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

    //TABS

    public static final ResourceKey<CreativeModeTab> WEAPONS_GUNS_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "weapons_tab")
    );

    public static final CreativeModeTab WEAPONS_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            WEAPONS_GUNS_TAB_KEY,
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup." + Weapons.MOD_ID + ".weapons_tab"))
                    .icon(() -> new ItemStack(ModItems.AK47))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.GLOCK17);
                        output.accept(ModItems.AK47);
                        output.accept(ModItems.AWM);
                    })
                    .build()

    );

    public static final ResourceKey<CreativeModeTab> WEAPONS_AMMO_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "weapons_ammo_tab")
    );

    public static final CreativeModeTab WEAPONS_AMMO_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            WEAPONS_AMMO_TAB_KEY,
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                    .title(Component.translatable("itemGroup." + Weapons.MOD_ID + ".ammo"))
                    .icon(() -> new ItemStack(ModItems.AK47_MAGAZINE))
                    .build()
    );

    //AMMO
    private static final ResourceKey<Item> AMMO_KEY = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "ammo")
    );

    public static final Item AMMO = Registry.register(
            BuiltInRegistries.ITEM,
            AMMO_KEY,
            new AmmoItem(new Item.Properties().setId(AMMO_KEY).stacksTo(16))
    );

    //MAGS
    private static final ResourceKey<Item> AK47_MAGAZINE_KEY = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "ak47-mag")
    );

    public static final Item AK47_MAGAZINE = Registry.register(
            BuiltInRegistries.ITEM,
            AK47_MAGAZINE_KEY,
            new AmmoItem(new Item.Properties().setId(AK47_MAGAZINE_KEY).stacksTo(4))
    );

    private static final ResourceKey<Item> AWM_MAGAZINE_KEY = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "awm_mag")
    );

    public static final Item AWM_MAGAZINE = Registry.register(
            BuiltInRegistries.ITEM,
            AWM_MAGAZINE_KEY,
            new AmmoItem(new Item.Properties().setId(AWM_MAGAZINE_KEY).stacksTo(4))
    );

    private static final ResourceKey<Item> GLOCK_MAG_KEY = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "glock_mag")
    );

    public static final Item GLOCK_MAG = Registry.register(
            BuiltInRegistries.ITEM,
            GLOCK_MAG_KEY,
            new AmmoItem(new Item.Properties().setId(GLOCK_MAG_KEY).stacksTo(16))
    );

    //GUNS
    private static final ResourceKey<Item> AK47_KEY = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "ak-47")
    );

    public static final Item AK47 = Registry.register(
            BuiltInRegistries.ITEM,
            AK47_KEY,
            new GunItem(new Item.Properties().setId(AK47_KEY).stacksTo(1),
                    35.0f, 13.0f, 0.0085f, 0.99f, 30, AK47_MAGAZINE, 45, 2, false, ".7,62x39")
    );

    private static final ResourceKey<Item> AWM_KEY = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "awm")
    );

    public static final Item AWM = Registry.register(
            BuiltInRegistries.ITEM,
            AWM_KEY,
            new GunItem(new Item.Properties().setId(AWM_KEY).stacksTo(1),
                    47.0f, 30.0f, 0.0085f, 0.99f, 5, AWM_MAGAZINE, 70, 25, true, ".7,62×67")
    );

    private static final ResourceKey<Item> GLOCK_17_KEY = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(Weapons.MOD_ID, "glock_17")
    );

    public static final Item GLOCK17 = Registry.register(
            BuiltInRegistries.ITEM,
            GLOCK_17_KEY,
            new GunItem(new Item.Properties().setId(GLOCK_17_KEY).stacksTo(1),
                    15.0f, 10.0f, 0.0245f, 0.99f, 17, GLOCK_MAG, 30, 10, false,".9×19")
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
            output.accept(AWM);
        });

        net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents.modifyOutputEvent(WEAPONS_AMMO_TAB_KEY).register(output -> {
            output.accept(AMMO);
            output.accept(GLOCK_MAG);
            output.accept(AK47_MAGAZINE);
            output.accept(AWM_MAGAZINE);
        });

//        //GUNS
//        CreativeModeTabEvents.modifyOutputEvent(combatTab).register(output -> output.accept(TEST));
//
//        //AMMOS
//        CreativeModeTabEvents.modifyOutputEvent(combatTab).register(output -> output.accept(AMMO));
//        //MAGS
//        CreativeModeTabEvents.modifyOutputEvent(combatTab).register(output -> output.accept(GLOCK_MAG));
    }
}
