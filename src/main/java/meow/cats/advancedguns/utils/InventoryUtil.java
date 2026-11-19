package meow.cats.advancedguns.utils;

import meow.cats.advancedguns.items.AmmoItem;
import meow.cats.advancedguns.items.MagazineItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class InventoryUtil implements Utils{

    public static int findItemSlot(Item item) {
        for (int i = 9; i <= 44; i++) {
            if (mc.player.containerMenu.getSlot(i).getItem().getItem() == item) {
                return i;
            }
        }
        return -1;
    }

//    public static int findBestSlot(ItemStack item) {
//        int bestSlot = -1;
//        int ammoCount = -1;
//        for (int i = 9; i <= 44; i++) {
//            if (mc.player.containerMenu.getSlot(i).getItem() == item) {
//                if (item.getItem() instanceof MagazineItem magazineItem){
//                    if (bestSlot == -1) {bestSlot = i; ammoCount = magazineItem.getAmmoCount(item);}
//                    else if (magazineItem.getAmmoCount(item) > ammoCount) {
//                        bestSlot = i;
//                        ammoCount = magazineItem.getAmmoCount(item);
//                    }
//                } else if (item.getItem() instanceof AmmoItem) return i;
//            }
//        }
//        return bestSlot;
//    }

    public static int findBestSlot(Player player, Item targetItem) {
        int bestSlot = -1;
        int maxAmmo = -1;
        Inventory inv = player.getInventory();
        for (int i = 0; i <= 44; i++) {
            ItemStack stackInSlot = inv.getItem(i);
            if (!stackInSlot.isEmpty() && stackInSlot.getItem() == targetItem) {
                if (targetItem instanceof MagazineItem magazineItem) {
                    int currentMagAmmo = magazineItem.getAmmoCount(stackInSlot);
                    if (currentMagAmmo > maxAmmo) {
                        bestSlot = i;
                        maxAmmo = currentMagAmmo;
                    }
                } else if (targetItem instanceof AmmoItem) {
                    return i;
                }
            }
        }
        return bestSlot;
    }
}
