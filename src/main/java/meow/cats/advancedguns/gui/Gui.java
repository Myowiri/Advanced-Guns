package meow.cats.advancedguns.gui;

import meow.cats.CatGui;
import meow.cats.advancedguns.config.ClientConfig;
import meow.cats.items.BooleanButton;

public class Gui extends CatGui {

    public static Gui Instance;

    public Gui() {
        Instance = this;
    }

    static{
        Instance = new Gui();
    }

    @Override
    public int getStartY(){
        return this.getScreenHeight()/2 - getSpacing() - getHeight();
    }

    @Override
    public void addButtons() {
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.hud, "Hud", value -> {
            ClientConfig.INSTANCE.hud = value;
            ClientConfig.save();
        }
        ));
        buttons.add(new BooleanButton(ClientConfig.INSTANCE.hud, "Vanilla Crosshair", value -> {
            ClientConfig.INSTANCE.Crosshair = value;
            ClientConfig.save();
        }
        ));
    }

}
