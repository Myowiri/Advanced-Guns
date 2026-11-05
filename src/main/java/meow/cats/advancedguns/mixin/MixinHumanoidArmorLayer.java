//package meow.cats.advancedguns.mixin;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import meow.cats.advancedguns.items.GunItem;
//import net.minecraft.client.model.HumanoidModel;
//import net.minecraft.client.renderer.SubmitNodeCollector;
//import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
//import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
//import net.minecraft.client.renderer.state.level.PlayerRenderState;
//import net.minecraft.world.entity.EquipmentSlot;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(HumanoidArmorLayer.class)
//public abstract class MixinHumanoidArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> {
//
//    @Shadow protected abstract A getArmorModel(final S state, final EquipmentSlot slot);
//
//    @Inject(method = "submit", at = @At("HEAD"))
//    private void applyGunPoseThirdPerson(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final S state, final float yRot, final float xRot, CallbackInfo ci) {
//        if ((Object) state instanceof PlayerRenderState playerState) {
//            if (playerState.firstPersonHandsAndItems.mainHandItem != null && playerState.firstPersonHandsAndItems.mainHandItem.getItem() instanceof GunItem) {
//                for (EquipmentSlot slot : EquipmentSlot.values()) {
//                    if (slot.isArmor()) {
//                        A model = this.getArmorModel(state, slot);
//                        if (model != null) {
//                            if (playerState.avatarRenderState.mainArm == net.minecraft.world.entity.HumanoidArm.RIGHT) {
//                                model.rightArm.xRot = -1.05F;
//                                model.rightArm.yRot = -0.35F;
//                                model.leftArm.xRot = -0.95F;
//                                model.leftArm.yRot = 0.45F;
//                            } else {
//                                model.leftArm.xRot = -1.05F;
//                                model.leftArm.yRot = 0.35F;
//                                model.rightArm.xRot = -0.95F;
//                                model.rightArm.yRot = -0.45F;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
