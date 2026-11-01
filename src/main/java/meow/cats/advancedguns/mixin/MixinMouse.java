package meow.cats.advancedguns.mixin;

import meow.cats.Weapons;
import meow.cats.advancedguns.items.GunItem;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meow.cats.advancedguns.utils.Utils.mc;

@Mixin(MouseHandler.class)
public class MixinMouse {

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (mc == null || mc.player == null || Weapons.zoom == null) {
            return;
        }

        if (Weapons.zoom.active) {
            ItemStack handStack = mc.player.getMainHandItem();

            if (handStack != null && handStack.getItem() instanceof GunItem gun && gun.hasScope()) {
                if (vertical > 0) {
                    Weapons.zoom.zoomIn();
                } else if (vertical < 0) {
                    Weapons.zoom.zoomOut();
                }
                ci.cancel();
            }
        }
    }
}
