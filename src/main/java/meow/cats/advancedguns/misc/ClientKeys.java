package meow.cats.advancedguns.misc;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class ClientKeys {
    public static final KeyMapping.Category WEAPONS_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("weapons", "keys"));
    public static final KeyMapping RELOAD_BIND = new KeyMapping("key.weapons.reload", InputConstants.Type.KEYBOARD, InputConstants.KEY_R, WEAPONS_CATEGORY);
    public static final KeyMapping CHECK_BIND = new KeyMapping("key.weapons.check", InputConstants.Type.KEYBOARD, InputConstants.KEY_Y, WEAPONS_CATEGORY);
    public static final KeyMapping ZOOM_BIND = new KeyMapping("key.weapons.zoom", InputConstants.Type.KEYBOARD, InputConstants.KEY_G, WEAPONS_CATEGORY);

}