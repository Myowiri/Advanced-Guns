package meow.cats.advancedguns.gui;

import meow.cats.CatGui;
import meow.cats.advancedguns.config.ClientConfig;
import meow.cats.items.*;
import net.minecraft.network.chat.Component;

public class Gui extends CatGui {


    public enum PAGE{
        Main,
        Crosshair
    }

    public PAGE currentPage = PAGE.Crosshair;

    public static Gui Instance;

    public Gui() {
        Instance = this;
    }

    static{
        Instance = new Gui();
    }

    @Override
    public int getStartY(){
        return 60;
    }


    @Override
    public void addButtons() {

        buttons.add(new Button(Component.translatable("gui.advancedguns.main"), v -> {currentPage = PAGE.Main;refreshButtons();}).setSecondColor(currentPage == PAGE.Main ? getSecondColor() : getMainColor()).doNotSpace().setWidth(80));

        buttons.add(new Button(Component.translatable("gui.advancedguns.crosshair"), v -> {currentPage = PAGE.Crosshair;refreshButtons();}).setSecondColor(currentPage == PAGE.Crosshair ? getSecondColor() : getMainColor()).setDx(80).setWidth(80));


        if (currentPage == PAGE.Main) mainPage();

        if (currentPage == PAGE.Crosshair) crosshairPage();


    }



    public void mainPage(){
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.toggleAim, Component.translatable("gui.advancedguns.toogleaim"), value -> {ClientConfig.INSTANCE.toggleAim = value;ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.hud, Component.translatable("gui.advancedguns.hud"), value -> {ClientConfig.INSTANCE.hud = value;ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.hints, Component.translatable("gui.advancedguns.hints"), value -> {ClientConfig.INSTANCE.hints = value;ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.coolDown, Component.translatable("gui.advancedguns.cooldown"), value -> {ClientConfig.INSTANCE.coolDown = value;ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.magIndicator, Component.translatable("gui.advancedguns.magindicator"), value -> {ClientConfig.INSTANCE.magIndicator = value;ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.ammoCounter, Component.translatable("gui.advancedguns.ammocounter"), value -> {ClientConfig.INSTANCE.ammoCounter = value;ClientConfig.save();}));


//        buttons.add(new Slider("mhX", ClientConfig.INSTANCE.mhX, -10, 10, v -> {ClientConfig.INSTANCE.mhX = v; ClientConfig.save();} ));
//        buttons.add(new Slider("mhY", ClientConfig.INSTANCE.mhY, -10, 10, v -> {ClientConfig.INSTANCE.mhY = v; ClientConfig.save();} ));
//        buttons.add(new Slider("mhZ", ClientConfig.INSTANCE.mhZ, -10, 10, v -> {ClientConfig.INSTANCE.mhZ = v; ClientConfig.save();} ));
//        buttons.add(new Slider("mhRX", ClientConfig.INSTANCE.mhRX, -180, 180, v -> {ClientConfig.INSTANCE.mhRX = v; ClientConfig.save();} ));
//        buttons.add(new Slider("mhRY", ClientConfig.INSTANCE.mhRY, -180, 180, v -> {ClientConfig.INSTANCE.mhRY = v; ClientConfig.save();} ));

    }

    public void crosshairPage(){
        buttons.add(new EnumButton<ClientConfig.CrosshairStyle>(ClientConfig.INSTANCE.crosshairStyle, Component.translatable("gui.advancedguns.style"), value -> {ClientConfig.INSTANCE.crosshairStyle = value;ClientConfig.save();}));
        buttons.add(new ColorPicker(ClientConfig.INSTANCE.getCrosshairColor(), Component.translatable("gui.advancedguns.color"), v -> {ClientConfig.INSTANCE.setCrosshairColor(v);ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.centerDot, Component.translatable("gui.advancedguns.centerdot"), v ->{ClientConfig.INSTANCE.centerDot = v; ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.lines, Component.translatable("gui.advancedguns.lines"), v ->{ClientConfig.INSTANCE.lines = v; ClientConfig.save();}));
        buttons.add(new Slider(Component.translatable("gui.advancedguns.slider"), ClientConfig.INSTANCE.scale, .7f, 3f, v -> {ClientConfig.INSTANCE.scale = v; ClientConfig.save();} ));
        buttons.add(new IntegerSlider(Component.translatable("gui.advancedguns.width"), ClientConfig.INSTANCE.width, 0, 10, v -> {ClientConfig.INSTANCE.width = Math.round(v); ClientConfig.save();} ));
        buttons.add(new IntegerSlider(Component.translatable("gui.advancedguns.padding"), ClientConfig.INSTANCE.padding, 0, 10, v -> {ClientConfig.INSTANCE.padding = Math.round(v); ClientConfig.save();} ));
    }

}
