package meow.cats.advancedguns.event;


import meow.cats.Weapons;
import meow.cats.advancedguns.event.impl.ShootEvent;
import meow.cats.advancedguns.event.impl.TickEvent;
import meow.cats.advancedguns.items.GunItem;
import meow.cats.advancedguns.items.MagazineItem;
import meow.cats.advancedguns.misc.ClientKeys;
import meow.cats.advancedguns.network.ReloadPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import static meow.cats.advancedguns.utils.Utils.EVENT_BUS;
import static meow.cats.advancedguns.utils.Utils.mc;


public class EventManager  {
    public void init() {
        EVENT_BUS.register(this);
    }

    public void onUnload() {
        EVENT_BUS.unregister(this);
    }


    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null )
            return;
        updateBinds(event.getPlayer());
    }

    public void updateBinds(LocalPlayer player){
        if (ClientKeys.RELOAD_BIND.consumeClick()) {
            if (player.getMainHandItem().getItem() instanceof GunItem || player.getMainHandItem().getItem() instanceof MagazineItem) {
                ClientPlayNetworking.send(new ReloadPayload());
            }
        }

        if (ClientKeys.CHECK_BIND.consumeClick()) {
            net.minecraft.world.item.ItemStack handStack = player.getMainHandItem();
            if (handStack.getItem() instanceof GunItem gun) {
                int currentAmmo = gun.getAmmoCount(handStack);
                int maxAmmo = gun.getMaxMag();

                String messageText = currentAmmo + " / " + maxAmmo;
                player.sendOverlayMessage(Component.literal(messageText));
            }
        }

        if (ClientKeys.ZOOM_BIND.consumeClick()) {
            net.minecraft.world.item.ItemStack handStack = player.getMainHandItem();
            if (handStack.getItem() instanceof GunItem gun) {
                Weapons.zoom.active = !Weapons.zoom.active;
            }
        }

    }

    @Subscribe
    public void onShoot(ShootEvent event){
    }


}