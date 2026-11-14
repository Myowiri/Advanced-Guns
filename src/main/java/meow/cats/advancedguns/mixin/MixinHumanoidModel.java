package meow.cats.advancedguns.mixin;

import meow.cats.advancedguns.items.GunItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class MixinHumanoidModel {

    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart head;

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void onSetupAnim(HumanoidRenderState state, CallbackInfo ci) {
        if (state instanceof AvatarRenderState avatarState) {
            boolean isRightMain = avatarState.mainArm == HumanoidArm.RIGHT;

            boolean holdingGunInMainHand = avatarState.getMainHandItemStack().getItem() instanceof GunItem;

            if (holdingGunInMainHand) {
                if (isRightMain) {
                    this.rightArm.xRot = this.head.xRot - (float) (Math.PI / 2.0);
                    this.rightArm.yRot = this.head.yRot;

                    this.leftArm.xRot = this.head.xRot - (float) (Math.PI / 2.1);
                    this.leftArm.yRot = this.head.yRot + 0.45F;
                } else {
                    this.leftArm.xRot = this.head.xRot - (float) (Math.PI / 2.0);
                    this.leftArm.yRot = this.head.yRot;

                    this.rightArm.xRot = this.head.xRot - (float) (Math.PI / 2.1);
                    this.rightArm.yRot = this.head.yRot - 0.45F;
                }
            }
        }
    }
}
