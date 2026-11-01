package meow.cats.advancedguns.mixin;

import meow.cats.ClientKeys;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meow.cats.ViewHolder.mc;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (this.player != null) {
            if (ClientKeys.SECOND_PERSON_FRONT.isDown()) {
                mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);
            }
            else if (ClientKeys.THIRD_PERSON_FRONT.isDown()) {
                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            }
            else mc.options.setCameraType(CameraType.FIRST_PERSON);
        }
    }

    @Redirect(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
    private boolean disablePerspectiveToggle(KeyMapping keyMapping) {
        if (keyMapping == Minecraft.getInstance().options.keyTogglePerspective) {
            return false;
        }

        return keyMapping.consumeClick();
    }
}