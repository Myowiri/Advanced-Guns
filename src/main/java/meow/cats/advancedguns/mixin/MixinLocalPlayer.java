package meow.cats.advancedguns.mixin;

import meow.cats.advancedguns.event.impl.TickEvent;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meow.cats.advancedguns.utils.Utils.EVENT_BUS;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = false)
    public void postTickEvent(CallbackInfo ci){
        TickEvent event = new TickEvent((LocalPlayer) (Object) this);
        event.setPlayer((LocalPlayer) (Object) this);
        EVENT_BUS.post(event);
    }

//    @Inject(method = "tick", at = @At("TAIL"))
//    private void handleCustomKeybinds(CallbackInfo ci) {
//        LocalPlayer player = (LocalPlayer) (Object) this;
//
//
//    }
}
