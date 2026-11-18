package meow.cats.advancedguns.event;


import meow.cats.Weapons;
import meow.cats.advancedguns.config.ClientConfig;
import meow.cats.advancedguns.event.impl.ShootEvent;
import meow.cats.advancedguns.event.impl.TickEvent;
import meow.cats.advancedguns.items.GunItem;
import meow.cats.advancedguns.items.MagazineItem;
import meow.cats.advancedguns.misc.ClientKeys;
import meow.cats.advancedguns.network.ReloadPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

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

        doShoot();

    }


    public void updateBinds(LocalPlayer player){
        if (ClientKeys.RELOAD_BIND.consumeClick()) {
            if (player.getMainHandItem().getItem() instanceof GunItem || player.getMainHandItem().getItem() instanceof MagazineItem) {
                ClientPlayNetworking.send(new ReloadPayload());
            }
        }

        if (ClientKeys.CHECK_BIND.consumeClick()) {
            ItemStack handStack = player.getMainHandItem();
            if (handStack.getItem() instanceof GunItem gun) {
                int currentAmmo = gun.getAmmoCount(handStack);
                int maxAmmo = gun.getMaxMag();

                String messageText = currentAmmo + " / " + maxAmmo;
                player.sendOverlayMessage(Component.literal(messageText));
            }
        }
        if (ClientConfig.INSTANCE.toggleAim) {
        if (ClientKeys.ZOOM_BIND.consumeClick()) {
            ItemStack handStack = player.getMainHandItem();
            if (handStack.getItem() instanceof GunItem gun) {
                if (gun.canAim() || gun.hasScope())
                Weapons.zoom.active = !Weapons.zoom.active;
            }
        } } else {
            if (ClientKeys.ZOOM_BIND.isDown()){
                ItemStack handStack = player.getMainHandItem();
                if (handStack.getItem() instanceof GunItem gun) {
                    if (gun.canAim() || gun.hasScope())
                        Weapons.zoom.active = true;
                }
            } else Weapons.zoom.active = false;
        }

        if (ClientKeys.MODE_BIND.consumeClick()) {
            ItemStack handStack = player.getMainHandItem();
            if (handStack.getItem() instanceof GunItem gun) {
                gun.changeMode();
            }
        }

    }

    public static int test = 0;
    @Subscribe
    public void onShoot(ShootEvent event){
        test = 1;
    }

    public void doShoot(){

    }

}