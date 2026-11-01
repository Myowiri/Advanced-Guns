package meow.cats.advancedguns.mixin;

import meow.cats.advancedguns.items.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    private int rightClickDelay;

    @Inject(method = "tick", at = @At("TAIL"))
    private void removeRightClickDelay(CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;

        if (mc.player != null && mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof GunItem) {
            this.rightClickDelay = 0;
        }
    }
}
