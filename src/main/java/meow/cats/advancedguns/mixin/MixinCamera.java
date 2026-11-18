package meow.cats.advancedguns.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import meow.cats.Weapons;
import meow.cats.advancedguns.items.GunItem;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static meow.cats.advancedguns.utils.Utils.mc;

@Mixin(Camera.class)
public abstract class MixinCamera {

    @ModifyReturnValue(method = "calculateFov", at = @At("RETURN"))
    private float modifyFov(float original) {
        if (mc.options.getCameraType() != CameraType.FIRST_PERSON) return original;


        if (mc == null || mc.level == null || mc.player == null) {
            return original;
        }
        if (Weapons.zoom == null) {
            return original;
        }

        net.minecraft.world.item.ItemStack handStack = mc.player.getMainHandItem();
        if (handStack.getItem() instanceof GunItem gun)
            if (Weapons.zoom.active) {
                if (gun.hasScope()) {
                    return Weapons.zoom.getCurrentFov();
                } else {
                    if (gun.canAim()) return original - original / 6;
                }
            }
        return original;
    }
}