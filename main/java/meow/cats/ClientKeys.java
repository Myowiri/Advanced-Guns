package meow.cats;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class ClientKeys {
    public static final KeyMapping.Category CATS_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("view-holder", "keys"));
    public static final KeyMapping THIRD_PERSON_FRONT = new KeyMapping("key.view-holder.third_person_front", InputConstants.Type.KEYBOARD, 62, CATS_CATEGORY);
    public static final KeyMapping SECOND_PERSON_FRONT = new KeyMapping("key.view-holder.second_person_front", InputConstants.Type.KEYBOARD, 63, CATS_CATEGORY);
}