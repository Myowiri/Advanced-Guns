package meow.cats.advancedguns.gui;

import meow.cats.CatGui;
import meow.cats.advancedguns.config.ClientConfig;
import meow.cats.items.*;

public class Gui extends CatGui {


    public enum PAGE{
        Hud,
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

        buttons.add(new Button("Hud", v -> {currentPage = PAGE.Hud;refreshButtons();}).setSecondColor(currentPage == PAGE.Hud ? getSecondColor() : getMainColor()).doNotSpace().setWidth(80));

        buttons.add(new Button("Crosshair", v -> {currentPage = PAGE.Crosshair;refreshButtons();}).setSecondColor(currentPage == PAGE.Crosshair ? getSecondColor() : getMainColor()).setDx(80).setWidth(80));


        if (currentPage == PAGE.Hud) mainPage();

        if (currentPage == PAGE.Crosshair) crosshairPage();


    }

    public void mainPage(){
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.hud, "Hud", value -> {ClientConfig.INSTANCE.hud = value;ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.coolDown, "CoolDown", value -> {ClientConfig.INSTANCE.coolDown = value;ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.magIndicator, "Mag Indicator", value -> {ClientConfig.INSTANCE.magIndicator = value;ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.ammoCounter, "Ammo Counter", value -> {ClientConfig.INSTANCE.ammoCounter = value;ClientConfig.save();}));

    }

    public void crosshairPage(){
        buttons.add(new EnumButton<ClientConfig.CrosshairStyle>(ClientConfig.INSTANCE.crosshairStyle, "Style", value -> {ClientConfig.INSTANCE.crosshairStyle = value;ClientConfig.save();}));
        buttons.add(new ColorPicker(ClientConfig.INSTANCE.getCrosshairColor(), "Color", v -> {ClientConfig.INSTANCE.setCrosshairColor(v);ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.centerDot, "Center Dot", v ->{ClientConfig.INSTANCE.centerDot = v; ClientConfig.save();}));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.lines, "Lines", v ->{ClientConfig.INSTANCE.lines = v; ClientConfig.save();}));
        buttons.add(new Slider("Scale", ClientConfig.INSTANCE.scale, .7f, 3f, v -> {ClientConfig.INSTANCE.scale = v; ClientConfig.save();} ));
        buttons.add(new IntegerSlider("Length", ClientConfig.INSTANCE.width, 0, 10, v -> {ClientConfig.INSTANCE.width = Math.round(v); ClientConfig.save();} ));
        buttons.add(new IntegerSlider("Padding", ClientConfig.INSTANCE.padding, 0, 10, v -> {ClientConfig.INSTANCE.padding = Math.round(v); ClientConfig.save();} ));
    }

}
