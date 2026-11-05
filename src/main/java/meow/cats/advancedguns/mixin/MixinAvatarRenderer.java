//package meow.cats.advancedguns.mixin;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import meow.cats.advancedguns.items.GunItem;
//import net.minecraft.client.renderer.SubmitNodeCollector;
//import net.minecraft.client.renderer.entity.player.AvatarRenderer;
//import net.minecraft.resources.Identifier;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(AvatarRenderer.class)
//public class MixinAvatarRenderer {
//
//    @Inject(method = "renderRightHand", at = @At("HEAD"))
//    private void applyRightHandPose(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, boolean hasSleeve, CallbackInfo ci) {
//        if (net.minecraft.client.Minecraft.getInstance().player != null && net.minecraft.client.Minecraft.getInstance().player.getMainHandItem().getItem() instanceof GunItem) {
//            poseStack.translate(-0.1F, 0.2F, -0.1F);
//        }
//    }
//
//    @Inject(method = "renderLeftHand", at = @At("HEAD"))
//    private void applyLeftHandPose(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, boolean hasSleeve, CallbackInfo ci) {
//        if (net.minecraft.client.Minecraft.getInstance().player != null && net.minecraft.client.Minecraft.getInstance().player.getMainHandItem().getItem() instanceof GunItem) {
//            poseStack.translate(0.1F, 0.2F, -0.1F);
//        }
//    }
//}
