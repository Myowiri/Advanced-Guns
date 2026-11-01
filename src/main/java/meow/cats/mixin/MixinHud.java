package meow.cats.mixin;

import meow.cats.Weapons;
import meow.cats.items.GunItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static meow.cats.utils.Utils.mc;


@Mixin(Hud.class)
public class MixinHud {


    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void onHudRender(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (mc == null || mc.level == null || mc.player == null ) {
            return;
        }
        if (Weapons.zoom == null) {
            return;
        }
        net.minecraft.world.item.ItemStack handStack = mc.player.getMainHandItem();
        if (handStack.getItem() instanceof GunItem gun)
            if (gun.hasScope())
                if (Weapons.zoom.active) {
                    int screenHeight = mc.getWindow().getGuiScaledHeight();
                    int screenWidth = mc.getInstance().getWindow().getGuiScaledWidth();
                    int a = (int)(screenHeight/2.5);
                    graphics.fill(0, 0, screenWidth/2-a, screenHeight, 0xFF000000);
                    graphics.fill(screenWidth/2+a, 0, screenWidth, screenHeight, 0xFF000000);
                    graphics.fill(0, 0, screenWidth, screenHeight/2-a, 0xFF000000);
                    graphics.fill(0, screenHeight/2+a, screenWidth, screenHeight, 0xFF000000);
                    graphics.fill(0, screenHeight/2-1, screenWidth, screenHeight/2+1, 0xFF000000);
                    graphics.fill(screenWidth/2-1, 0, screenWidth/2+1, screenWidth, 0xFF000000);
                }


//        EVENT_BUS.post(new Render2DEvent(graphics, deltaTracker.getGameTimeDeltaPartialTick(true)));
    }

    @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
    private void onExtractCrosshair(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (mc == null || mc.level == null || mc.player == null ) {
            return;
        }
        net.minecraft.world.item.ItemStack handStack = mc.player.getMainHandItem();
        if (handStack.getItem() instanceof GunItem gun)
            if (gun.hasScope()) ci.cancel();
    }
}