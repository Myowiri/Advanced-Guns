package meow.cats.mixin;

import meow.cats.event.impl.TickEvent;
import meow.cats.items.GunItem;
import meow.cats.misc.ClientKeys;
import meow.cats.network.ReloadPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meow.cats.utils.Utils.EVENT_BUS;

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
