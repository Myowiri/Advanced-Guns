package meow.cats.advancedguns.items;

import meow.cats.advancedguns.projectile.GrenadeEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class GrenadeItem extends Item {
    private final GrenadeType type;

    public GrenadeItem(Properties properties, GrenadeType type) {
        super(properties);
        this.type = type;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.FAIL;
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int heldTicks = this.getUseDuration(stack, entity) - timeLeft;
            throwGrenade(level, player, stack, heldTicks);
            return true;
        }
        return false;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof Player player && type != GrenadeType.MOLOTOV && type != GrenadeType.SMOKE) {
            int heldTicks = this.getUseDuration(stack, entity) - remainingUseDuration;
            if (heldTicks >= type.fuseTicks) {
                throwGrenade(level, player, stack, heldTicks);
                player.stopUsingItem();
            }
        }
    }

    private void throwGrenade(Level level, Player player, ItemStack stack, int heldTicks) {
        if (!level.isClientSide()) {
            int remainingFuse = (type == GrenadeType.MOLOTOV || type == GrenadeType.SMOKE)
                    ? type.fuseTicks
                    : Math.max(1, type.fuseTicks - heldTicks);

            GrenadeEntity grenade = new GrenadeEntity(level, player, type, remainingFuse, stack.getItem());
            grenade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.35f, 1.0f);
            level.addFreshEntity(grenade);
        }
        player.getCooldowns().addCooldown(stack, 20);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

//    @Override
//    public ItemUseAnimation getUseAnimation(ItemStack stack) {
//        return ItemUseAnimation.BOW;
//    }

//    @Override
//    public int getUseDuration(ItemStack stack, LivingEntity entity) {
//        return 72000;
//    }
}