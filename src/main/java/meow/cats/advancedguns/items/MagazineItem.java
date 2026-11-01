package meow.cats.advancedguns.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

import static meow.cats.advancedguns.items.ModItems.AMMO;

public class MagazineItem extends Item {

    public final int maxAmmo;
    public Item ammoItem = AMMO;


    public MagazineItem(Properties properties, int maxAmmo) {
        super(properties);
        this.maxAmmo = maxAmmo;
    }

    public MagazineItem setAmmoItem(Item item){ammoItem = item;return this;}
    public Item getAmmoItem(){return ammoItem;}

    public int getAmmoCount(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag nbt = customData.copyTag();
            if (nbt.contains("AmmoCount")) {
                return nbt.getInt("AmmoCount").orElse(0);
            }
        }
        return 0;
    }


    public void addAmmo(Player player, ItemStack magazineStack) {
        ItemStack offhandStack = player.getOffhandItem();
        if (!offhandStack.isEmpty() && offhandStack.getItem() == this.ammoItem) {
            int currentAmmo = getAmmoCount(magazineStack);
            if (currentAmmo >= this.maxAmmo) return;
            int newAmmo = currentAmmo + 1;
            setAmmoCount(magazineStack, newAmmo);
            offhandStack.shrink(1);
            if (offhandStack.isEmpty()) {
                player.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND, ItemStack.EMPTY);
            }

            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                serverPlayer.inventoryMenu.broadcastChanges();
            }
            String messageText = newAmmo + " / " + this.maxAmmo;
            player.sendOverlayMessage(Component.literal(messageText));
        }
    }

    public void setAmmoCount(ItemStack stack, int count) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
            nbt.putInt("AmmoCount", Math.max(0, Math.min(count, getMaxAmmo())));
        });
    }

    public int getMaxAmmo(){return maxAmmo;}

    @Override
    public void appendHoverText(final ItemStack itemStack, final TooltipContext context, final TooltipDisplay display, final Consumer<Component> builder, final TooltipFlag tooltipFlag) {
        int currentAmmo = getAmmoCount(itemStack);

        builder.accept(Component.literal(currentAmmo + " / " + this.maxAmmo)
                .withStyle(ChatFormatting.YELLOW));
    }

}
