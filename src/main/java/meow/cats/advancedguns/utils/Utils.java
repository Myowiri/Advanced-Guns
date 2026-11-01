package meow.cats.advancedguns.utils;

import meow.cats.advancedguns.event.EventBus;
import net.minecraft.client.Minecraft;

public interface Utils {
    Minecraft mc = Minecraft.getInstance();
    EventBus EVENT_BUS = new EventBus();
}
