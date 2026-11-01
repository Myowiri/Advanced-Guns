package meow.cats.utils;

import meow.cats.event.EventBus;
import net.minecraft.client.Minecraft;

public interface Utils {
    Minecraft mc = Minecraft.getInstance();
    EventBus EVENT_BUS = new EventBus();
}
