package meow.cats.advancedguns.gui;

import meow.cats.Weapons;
import meow.cats.advancedguns.config.ClientConfig;
import meow.cats.advancedguns.items.GunItem;
import net.minecraft.client.CameraType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

import static meow.cats.advancedguns.utils.Utils.mc;

public class HudRender {


    public static void render(GuiGraphicsExtractor gge){
        if(RenderScope(gge)) return;
        renderHud(gge);
    }

    public static int getScreenHeight(){return mc.getWindow().getGuiScaledHeight();}
    public static int getScreenWidth(){
        return mc.getWindow().getGuiScaledWidth();
    }


    public static void renderCrosshair(GuiGraphicsExtractor gge) {
        if (mc.player == null) return;
        if (mc.options.getCameraType() != CameraType.FIRST_PERSON) return;
        ItemStack handStack = mc.player.getMainHandItem();
        if (handStack.getItem() instanceof GunItem gun && gun.hasScope()) return;

        ItemStack handStack2 = mc.player.getOffhandItem();
        if (handStack2.getItem() instanceof GunItem gun && gun.hasScope()) return;

        if (ClientConfig.INSTANCE.crosshairStyle != ClientConfig.CrosshairStyle.Custom) return;

        float invScale = (float) (1.0 / mc.getWindow().getGuiScale());
        int cx = mc.getWindow().getWidth() / 2;
        int cy = mc.getWindow().getHeight() / 2;

        int color = ClientConfig.INSTANCE.getCrosshairColor().getRGB();
        float s = ClientConfig.INSTANCE.scale;
        int thickness = 2;
        int half = thickness / 2;

        gge.pose().pushMatrix();
        gge.pose().scale(invScale, invScale);
        gge.pose().translate(cx, cy);
        gge.pose().scale(s, s);
        gge.pose().translate(-cx, -cy);

        if (ClientConfig.INSTANCE.centerDot) {
            gge.fill(cx - half, cy - half, cx - half + thickness, cy - half + thickness, color);
        }

        if (ClientConfig.INSTANCE.lines) {
            int pad = ClientConfig.INSTANCE.padding;
            int len = ClientConfig.INSTANCE.width;
            gge.fill(cx - len - pad, cy - half, cx - pad, cy - half + thickness, color);
            gge.fill(cx - half, cy - len - pad, cx - half + thickness, cy - pad, color);
            gge.fill(cx + pad, cy - half, cx + len + pad, cy - half + thickness, color);
            gge.fill(cx - half, cy + pad, cx - half + thickness, cy + len + pad, color);
        }

        gge.pose().popMatrix();
    }



    public static boolean RenderScope(GuiGraphicsExtractor graphics){

        if (mc.options.getCameraType() != CameraType.FIRST_PERSON) return false;

        if (Weapons.zoom == null) {
            return false;
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
                    return true;
                }
        return false;
    }





    public static void renderHud(GuiGraphicsExtractor graphics){

        if (!ClientConfig.INSTANCE.hud) return;

        ItemStack heldStack = mc.player.getMainHandItem();

        if (heldStack.getItem() instanceof GunItem gun){


            int ammoCount = gun.getAmmoCount(heldStack);
            int maxAmmoCount = gun.getMaxMag();

            int coolDown = (int) (mc.player.getCooldowns().getCooldownPercent(heldStack, 0)*10);

            String ammoCountText = ammoCount + "/" + maxAmmoCount;
            String hasMagText = Boolean.toString(gun.hasMagRn(heldStack));
            String coolDownString = Integer.toString(coolDown);
            String currentMode = gun.getCurrentMode().toString();

            int y = 10;

            if (ClientConfig.INSTANCE.ammoCounter){graphics.text(mc.font, ammoCountText, getScreenWidth()-1-mc.font.width(ammoCountText) , getScreenHeight()-y, -1); y +=10;}
            if (ClientConfig.INSTANCE.magIndicator){graphics.text(mc.font, hasMagText, getScreenWidth()-1-mc.font.width(hasMagText) , getScreenHeight()-y, -1); y+=10;}
            if (ClientConfig.INSTANCE.mode){graphics.text(mc.font, currentMode, getScreenWidth()-1-mc.font.width(currentMode) , getScreenHeight()-y, -1); y+=10;}
            if (ClientConfig.INSTANCE.coolDown){graphics.text(mc.font, coolDownString, getScreenWidth()-1-mc.font.width(coolDownString)  , getScreenHeight()-y, -1); y+=10;}

        }

    }

}
