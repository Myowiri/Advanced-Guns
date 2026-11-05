package meow.cats.advancedguns.items;

import meow.cats.advancedguns.config.Config;
import meow.cats.advancedguns.event.impl.ShootEvent;
import meow.cats.advancedguns.projectile.BulletEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Prediction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import java.util.function.Consumer;
import static meow.cats.advancedguns.items.ModItems.AMMO;
import static meow.cats.advancedguns.misc.sounds.MUTED_SINGLE_SHOT;
import static meow.cats.advancedguns.misc.sounds.PISTOL_RELOAD;
import static meow.cats.advancedguns.utils.Utils.EVENT_BUS;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GunItem extends Item {

    private final Supplier<Config.WeaponConfig> configSupplier;
//    private final float bulletSpeed;
//    private final float bulletDamage;
//    private final float gravity;
//    private final float airResistance;
    private final boolean scope;
    private final String toolTipText;
//    public final int reloadCoolDown;
//    public final int shotCoolDown;
//    public final int getMaxMag();
    public final Item mag;
    @Deprecated public int ammoCount;
    public Item ammo = AMMO;
    public boolean explode = false;
    private boolean noMag = false;
    private boolean canAim = true;
//    public GunItem(Properties properties, float bulletSpeed, float bulletDamage, float gravity, float airResistance, int getMaxMag(), Item mag, int reloadCoolDown, int shotCoolDown, boolean scope,  String toolTipText) {
//        super(properties);
//
//        this.bulletSpeed = bulletSpeed;
//        this.bulletDamage = bulletDamage;
//        this.gravity = gravity;
//        this.airResistance = airResistance;
//        this.getMaxMag() = getMaxMag();
//        this.mag = mag;
//        this.shotCoolDown = shotCoolDown;
//        this.reloadCoolDown = reloadCoolDown;
//        this.toolTipText = toolTipText;
//        this.scope = scope;
//    }
    public GunItem(Properties properties, Supplier<Config.WeaponConfig> configSupplier, boolean hasScope, Item mag, String toolTipText) {
        super(properties);
        this.configSupplier = configSupplier;
        this.scope = hasScope;
        this.toolTipText = toolTipText;
        this.mag = mag;
    }

    private Config.WeaponConfig getConfig() {
        return this.configSupplier.get();
    }

    public float getBulletSpeed() { return getConfig().getSpeed(); }
    public float getBulletDamage() { return getConfig().getDamage(); }
    public float getGravity() { return getConfig().getGravity(); }
    public float getAirResistance() { return getConfig().getResistance(); }
    public int getMaxMag() { return getConfig().getMaxMag(); }
    public int getShotCoolDown() { return getConfig().getShotCooldown(); }
    public int getReloadCoolDown() { return getConfig().getReloadCooldown(); }



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

    public void setAmmoCount(ItemStack stack, int count) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
            nbt.putInt("AmmoCount", Math.max(0, Math.min(count, getMaxMag())));
        });
    }

    public GunItem shouldExplode(){
        this.explode = true;
        return this;
    }

    public boolean Explode(){
        return explode;
    }


    public GunItem removeMag(){
        noMag = true;
        return this;
    }

    public boolean hasScope(){
        return scope;
    }

    public Item getAmmo(){
        return ammo;
    }

    public GunItem setAmmo(Item item){
        ammo = item;
        return this;
    }

    public GunItem forbidAim(){canAim = false; return this;}

    public boolean canAim(){ return canAim;}

    public boolean hasMagRn(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag nbt = customData.copyTag();
            if (nbt.contains("Mag")) {
                return nbt.getBoolean("Mag").get();
            }
        }
        return false;
    }

    public void addOrRemoveMag(ItemStack stack, boolean bool) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
            nbt.putBoolean("Mag", bool);
        });
    }

    public void reload(Player player, ItemStack gunStack) {
        if (player.getCooldowns().isOnCooldown(gunStack.getItem().getDefaultInstance())) return;

        if (hasMagRn(gunStack)) {
            if (!noMag) {
                MagazineItem requiredMag = (MagazineItem) this.mag;
                int ammoToSave = getAmmoCount(gunStack);
                ItemStack magStack = new ItemStack(requiredMag);
                requiredMag.setAmmoCount(magStack, ammoToSave);
                if (freeSlot(player)) player.drop(magStack, true, Prediction.PREDICTED);
                else player.drop(magStack, false, Prediction.PREDICTED);
            } else {
                AmmoItem requiredAmmo = (AmmoItem) this.ammo;
                int ammoToSave = getAmmoCount(gunStack);
                ItemStack ammoStack = new ItemStack(requiredAmmo, ammoToSave);
                if (freeSlot(player)) player.drop(ammoStack, true, Prediction.PREDICTED);
                else player.drop(ammoStack, false, Prediction.PREDICTED);
            }

            addOrRemoveMag(gunStack, false);
            setAmmoCount(gunStack, 0);
            player.sendOverlayMessage(Component.literal("0 / " + getMaxMag()));

            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                serverPlayer.inventoryMenu.broadcastChanges();
            }
            return;
        }

        if (player.getAbilities().instabuild) {
            setAmmoCount(gunStack, getMaxMag());
            addOrRemoveMag(gunStack, true);
            return;
        }

        int targetAmmo = -1;
        boolean actionSuccessful = false;

        if (noMag) {
            AmmoItem requiredAmmo = (AmmoItem) this.ammo;
            int currentAmmo = getAmmoCount(gunStack);
            int neededAmmo = getMaxMag() - currentAmmo;

            if (neededAmmo > 0) {
                ItemStack offhandStack = player.getOffhandItem();
                if (!offhandStack.isEmpty() && offhandStack.getItem() == requiredAmmo) {
                    int take = 1;
                    offhandStack.shrink(1);
                    if (offhandStack.isEmpty()) {
                        player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                    }
                    targetAmmo = currentAmmo + take;
                    actionSuccessful = true;
                }
            }
        } else {
            MagazineItem requiredMag = (MagazineItem) this.mag;
            ItemStack offhandStack = player.getOffhandItem();

            if (!offhandStack.isEmpty() && offhandStack.getItem() == requiredMag) {
                int a = requiredMag.getAmmoCount(offhandStack);
                if (a >= 0) {
                    offhandStack.shrink(1);
                    if (offhandStack.isEmpty()) {
                        player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                    }
                    targetAmmo = a;
                    actionSuccessful = true;
                }
            }
        }

        if (actionSuccessful) {
            player.level().playSound(
                    null,
                    player.getX(),
                    player.getY() + player.getEyeHeight(),
                    player.getZ(),
                    PISTOL_RELOAD,
                    SoundSource.PLAYERS,
                    1.5f,
                    1.0f
            );

            player.getCooldowns().addCooldown(gunStack.getItem().getDefaultInstance(), getReloadCoolDown());
            addOrRemoveMag(gunStack, true);
            setAmmoCount(gunStack, targetAmmo);

            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                serverPlayer.inventoryMenu.broadcastChanges();
            }

            player.sendOverlayMessage(Component.literal(targetAmmo + " / " + getMaxMag()));
        }
    }






    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {

        return 72000;
    }

    @Override
    public void appendHoverText(final ItemStack itemStack, final TooltipContext context, final TooltipDisplay display, final Consumer<Component> builder, final TooltipFlag tooltipFlag) {
//        builder.accept(net.minecraft.network.chat.Component.empty());
        builder.accept(net.minecraft.network.chat.Component.translatable("tooltip.weapons.caliber" + toolTipText)
                .withStyle(net.minecraft.ChatFormatting.YELLOW));
        builder.accept(net.minecraft.network.chat.Component.literal(getAmmoCount(itemStack) + "/" + getMaxMag() + " " + Boolean.toString(hasMagRn(itemStack)))
                .withStyle(net.minecraft.ChatFormatting.YELLOW));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack gunStack = player.getItemInHand(hand);
        int currentAmmo = getAmmoCount(gunStack);
        boolean canShoot = currentAmmo > 0 || player.getAbilities().instabuild;
//        ItemStack itemstack = player.getItemInHand(hand);
//        player.startUsingItem(hand);
        if (canShoot) {
            if (!level.isClientSide()) {
                BulletEntity bullet = new BulletEntity(level, player, getBulletSpeed(), getBulletDamage(), getGravity(), getAirResistance()).shouldExplode(explode).setAmmo(ammo);
                EVENT_BUS.post(new ShootEvent());
                level.addFreshEntity(bullet);
                level.playSound(
                        null,
                        player.getX(),
                        player.getY() + player.getEyeHeight(),
                        player.getZ(),
                        MUTED_SINGLE_SHOT,
                        SoundSource.PLAYERS,
                        1.5f,
                        1.0f
                );

                if (!player.getAbilities().instabuild) {
                    setAmmoCount(gunStack, currentAmmo - 1);
                    String messageText = currentAmmo-1 + " / " + getMaxMag();
                    player.sendOverlayMessage(Component.literal(messageText));
                }


            }

            player.getCooldowns().addCooldown(gunStack.getItem().getDefaultInstance(), getShotCoolDown());
            if (noMag && currentAmmo-1 == 0) addOrRemoveMag(gunStack, false);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.FAIL;
    }

    private boolean freeSlot(Player player) {
        for (int i = 9; i <= 44; i++) {
            if (player.containerMenu.getSlot(i).getItem().isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
