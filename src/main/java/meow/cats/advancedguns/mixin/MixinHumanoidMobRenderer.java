//package meow.cats.advancedguns.mixin;
//
//import meow.cats.advancedguns.items.GunItem;
//import net.minecraft.client.model.HumanoidModel;
//import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
//import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
//import net.minecraft.world.entity.HumanoidArm;
//import net.minecraft.world.entity.Mob;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(HumanoidMobRenderer.class)
//public abstract class MixinHumanoidMobRenderer<T extends Mob, S extends HumanoidRenderState, M extends HumanoidModel<S>> {
//
//    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
//    private void applyGunPoseThirdPerson(final T mob, final HumanoidArm arm, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
//        if (mob.getMainArm() == arm && mob.getMainHandItem().getItem() instanceof GunItem) {
//            cir.setReturnValue(HumanoidModel.ArmPose.BOW_AND_ARROW);
//        }
//    }
//}
