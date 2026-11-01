package meow.cats.advancedguns.misc;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import static meow.cats.Weapons.MOD_ID;

public class sounds {

    public static final SoundEvent MUTED_SINGLE_SHOT = register("mutedsingleshot");
    public static final SoundEvent PISTOL_RELOAD = register("pistol_reload");

    public static void load() {

    }
    private static SoundEvent register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
