package meow.cats.advancedguns.misc;

import com.mojang.serialization.MapCodec;
import meow.cats.advancedguns.items.GunItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record HasMagazineProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<HasMagazineProperty> CODEC = MapCodec.unit(new HasMagazineProperty());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
        if (stack.getItem() instanceof GunItem gunItem) {
            return gunItem.hasMagRn(stack);
        }
        return false;
    }

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return CODEC;
    }
}
