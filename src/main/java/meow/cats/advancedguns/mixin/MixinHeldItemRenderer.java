package meow.cats.advancedguns.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import meow.cats.Weapons;
import meow.cats.advancedguns.config.ClientConfig;
import meow.cats.advancedguns.event.EventManager;
import meow.cats.advancedguns.items.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class MixinHeldItemRenderer {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract void renderPlayerArm(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float inverseArmHeight, float attackValue, HumanoidArm arm, PlayerRenderState playerState);

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER), cancellable = true)
    private void onSubmitArmWithItem(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (!Weapons.zoom.active) return;
        if (minecraft.player.getMainHandItem().getItem() instanceof GunItem gun && gun.canAim()) {
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
                poseStack.translate(gun.getAimX() / 16.0F, gun.getAimY()  / 16.0F, gun.getAimZ() / 16.0F);
//                ClientConfig c = ClientConfig.INSTANCE;
//                poseStack.translate(-9 / 16.0F, c.mhY / 16.0F, c.mhZ / 16.0F);
            }
        }
    }

    @Inject(method = "submitArmWithItem", at = @At("HEAD"), cancellable = true)
    private void lockWeaponHeightOnUpdate(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (minecraft.player.getMainHandItem().getItem() instanceof GunItem) {
            if (hand == InteractionHand.OFF_HAND) {
                ci.cancel();
                return;
            }
            if (hand == InteractionHand.MAIN_HAND) {
                poseStack.translate(0.0F, inverseArmHeight/ 1.7F, inverseArmHeight / 5.0F);
            }
        }
    }


    @Inject(
            method = "submitArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"
            )
    )
    private void renderArmsWithGun(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (hand != InteractionHand.MAIN_HAND) return;
        if (!(itemStack.getItem() instanceof GunItem gun)) return;
        AvatarRenderState avatarState = playerState.avatarRenderState;
        if (avatarState == null || avatarState.isInvisible) return;

        HumanoidArm mainArm = avatarState.mainArm;
        HumanoidArm offArm = mainArm.getOpposite();

//        float pr = (minecraft.player.getCooldowns().getCooldownPercent(new ItemStack(gun), 10f) / gun.getReloadCoolDown());
//        ClientConfig gun = ClientConfig.INSTANCE;
        if (gun.MhR()) {
            poseStack.pushPose();
            poseStack.translate(gun.getMhX(), gun.getMhY(), gun.getMhZ());
            poseStack.rotateDegrees(Axis.XP, gun.getMhRX());
            poseStack.rotateDegrees(Axis.YP, gun.getMhRY());
            this.renderPlayerArm(poseStack, submitNodeCollector, lightCoords, 0.0F, 0.0F, mainArm, playerState);
            poseStack.popPose();
        }

        if (gun.OhR()){
            poseStack.pushPose();
            poseStack.translate(gun.getOhX(), gun.getOhY(), gun.getOhZ());
            poseStack.rotateDegrees(Axis.XP, gun.getOhRX());
            poseStack.rotateDegrees(Axis.YP, gun.getOhRY());
            this.renderPlayerArm(poseStack, submitNodeCollector, lightCoords, 0.0F, 0.0F, offArm, playerState);
            poseStack.popPose();}
    }
}
