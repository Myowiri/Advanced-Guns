package meow.cats.advancedguns.mixin;

import meow.cats.advancedguns.items.GunItem;
import meow.cats.advancedguns.network.ShootPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow public LocalPlayer player;

    @Shadow @Final
    public Options options;

    @Unique
    private boolean weapons$wasAttackDown = false;

    @Shadow
    private int rightClickDelay;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onClientTick(CallbackInfo ci) {
        if (player == null) {
            weapons$wasAttackDown = false;
            return;
        }

        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof GunItem ||
                player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof GunItem) {
            this.rightClickDelay = 0;
        }

        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof GunItem gun) {
            boolean isAttackDown = this.options.keyAttack.isDown();

            if (gun.currentMode == GunItem.Mode.Auto) {
                if (isAttackDown) {
                    ClientPlayNetworking.send(new ShootPayload());
                }
            } else if (gun.currentMode == GunItem.Mode.Semi) {
                if (isAttackDown && !weapons$wasAttackDown) {
                    ClientPlayNetworking.send(new ShootPayload());
                }
            }

            weapons$wasAttackDown = isAttackDown;
        } else {
            weapons$wasAttackDown = false;
        }
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void onStartAttack(CallbackInfoReturnable<Boolean> cir) {
        if (player != null && player.getMainHandItem().getItem() instanceof GunItem) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void onContinueAttack(boolean leftClick, CallbackInfo ci) {
        if (player != null && player.getMainHandItem().getItem() instanceof GunItem) {
            ci.cancel();
        }
    }
}
