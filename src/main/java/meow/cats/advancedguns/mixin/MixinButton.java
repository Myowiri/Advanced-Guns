package meow.cats.advancedguns.mixin;

import meow.cats.items.Button;
import meow.cats.items.Item;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Button.class)
public abstract class MixinButton extends Item {

    @Shadow
    public int rx;

    @Shadow
    public int ry;

    public MixinButton(String name) {
        super(name);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void changeRender(GuiGraphicsExtractor context, int x, int y, CallbackInfo ci){
        ci.cancel();
        int width = this.getWidth();
        int height = this.getHeight();
        this.rx = (this.x != null ? this.x : x) + this.dx;
        this.ry = (this.y != null ? this.y : y) + this.dy;
        context.fill(this.rx, this.ry, this.rx + width, this.ry + height, this.getSecondColor().getRGB());
        int var10003 = this.rx + width/2;
        int var10004 = this.ry + (height - 9) / 2;
        context.centeredText(Minecraft.getInstance().font, this.name, var10003, var10004, -1);
    }

}
