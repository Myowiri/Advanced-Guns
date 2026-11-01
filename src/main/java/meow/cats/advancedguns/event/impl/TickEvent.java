package meow.cats.advancedguns.event.impl;

import meow.cats.advancedguns.event.Event;
import net.minecraft.client.player.LocalPlayer;

public class TickEvent extends Event {
    public LocalPlayer player;
    public TickEvent(LocalPlayer player){
        this.player = player;
    }

    public LocalPlayer getPlayer(){
        return player;
    }

    public TickEvent setPlayer(LocalPlayer player){
        this.player = player;
        return this;
    }

}