package meow.cats.advancedguns.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import meow.cats.Weapons;
import meow.cats.advancedguns.items.AmmoItem;
import meow.cats.advancedguns.items.GunItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meow.cats.advancedguns.utils.Utils.mc;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class MixinHeldItemRenderer {


    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER))
    private void onSubmitArmWithItem(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (mc.player.getMainHandItem().getItem() instanceof GunItem gun){
            if (!Weapons.zoom.active) return;
            if (!gun.canAim()) return;
            int y = gun.hasScope() ? -10 : 2;
            poseStack.rotateDegrees(Axis.XP, 0.0F);
            poseStack.rotateDegrees(Axis.YP, 0);
            poseStack.rotateDegrees(Axis.ZP, 0.0F);
            poseStack.translate(-9 / 16.0F, y / 16.0F, 0 / 16.0F);}
    }


}
