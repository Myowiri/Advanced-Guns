package meow.cats.advancedguns.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import meow.cats.Weapons;
import meow.cats.advancedguns.items.GunItem;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meow.cats.advancedguns.utils.Utils.mc;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class MixinHeldItemRenderer {

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER), cancellable = true)
    private void onSubmitArmWithItem(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (!Weapons.zoom.active) return;
        if (mc.player.getMainHandItem().getItem() instanceof GunItem gun && gun.canAim()) {
            if (hand == InteractionHand.OFF_HAND) {
                ci.cancel();
                return;
            }
            if (hand == InteractionHand.MAIN_HAND) {
                if (gun.hasScope()) {ci.cancel(); return;}
//                float y = gun.hasScope() ? -20 : gun.getAimY();
                poseStack.rotateDegrees(Axis.XP, 0.0F);
                poseStack.rotateDegrees(Axis.YP, 0);
                poseStack.rotateDegrees(Axis.ZP, 0.0F);
                poseStack.translate(gun.getAimX() / 16.0F, gun.getAimY() / 16.0F, gun.getAimZ() / 16.0F);
            }
        }
    }

    @Inject(method = "submitArmWithItem", at = @At("HEAD"), cancellable = true)
    private void lockWeaponHeightOnUpdate(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (mc.player.getMainHandItem().getItem() instanceof GunItem) {
            if (hand == InteractionHand.OFF_HAND) {
                ci.cancel();
                return;
            }
            if (hand == InteractionHand.MAIN_HAND) {
                poseStack.translate(0.0F, inverseArmHeight / 1.7F, inverseArmHeight / 5.0F);
            }
        }
    }
}
