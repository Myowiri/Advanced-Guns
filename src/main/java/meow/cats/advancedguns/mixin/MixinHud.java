package meow.cats.advancedguns.mixin;


import meow.cats.advancedguns.config.ClientConfig;
import meow.cats.advancedguns.gui.HudRender;
import meow.cats.advancedguns.items.GunItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static meow.cats.advancedguns.utils.Utils.mc;


@Mixin(Hud.class)
public class MixinHud {


    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void scopeRender(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (mc == null || mc.level == null || mc.player == null ) {
            return;
        }
        HudRender.render(graphics);
//        EVENT_BUS.post(new R2DTailEvent(graphics));

    }

    @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
    private void onExtractCrosshair(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (mc == null || mc.level == null || mc.player == null ) {
            return;
        }
        if (!ClientConfig.INSTANCE.Crosshair) ci.cancel();

        net.minecraft.world.item.ItemStack handStack = mc.player.getMainHandItem();
        if (handStack.getItem() instanceof GunItem gun)
            if (gun.hasScope())
                ci.cancel();
        net.minecraft.world.item.ItemStack handStack2 = mc.player.getOffhandItem();
        if (handStack2.getItem() instanceof GunItem gun)
            if (gun.hasScope())
                ci.cancel();
    }



}