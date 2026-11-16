package meow.cats.advancedguns.projectile;

import meow.cats.advancedguns.items.GrenadeType;
import meow.cats.advancedguns.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import java.util.List;

public class GrenadeEntity extends Projectile implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(GrenadeEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> TRIGGERED = SynchedEntityData.defineId(GrenadeEntity.class, EntityDataSerializers.BOOLEAN);
    private GrenadeType type = GrenadeType.FRAG;
    private int fuse;
    private int activeTicks = 0;

    public GrenadeEntity(EntityType<? extends GrenadeEntity> entityType, Level level) {
        super(entityType, level);
    }

    public GrenadeEntity(Level level, LivingEntity owner, GrenadeType type, int fuse, Item item) {
        super(ModEntities.GRENADE, level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        this.type = type;
        this.fuse = fuse;
        this.entityData.set(ITEM_STACK, new ItemStack(item));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ITEM_STACK, new ItemStack(ModItems.FRAG_GRENADE));
        builder.define(TRIGGERED, false);
    }

    @Override
    public ItemStack getItem() {
        return this.entityData.get(ITEM_STACK);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entityData.get(TRIGGERED)) {
            handleTriggeredState();
            return;
        }

        Vec3 pos = this.position();
        Vec3 motion = this.getDeltaMovement();
        Vec3 nextPos = pos.add(motion);

        BlockHitResult blockHit = this.level().clip(new ClipContext(pos, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.of(this)));
        Vec3 entityEnd = blockHit.getType() != HitResult.Type.MISS ? blockHit.getLocation() : nextPos;
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(this.level(), this, pos, entityEnd, this.getBoundingBox().expandTowards(motion).inflate(0.2), this::canHitEntity);

        if (entityHit != null) {
            handleEntityCollision(entityHit, motion);
        } else if (blockHit.getType() == HitResult.Type.BLOCK) {
            handleBlockCollision(blockHit, motion);
        } else {
            this.setPos(nextPos);
        }

        motion = this.getDeltaMovement();
        if (this.isInWater()) {
            motion = motion.scale(0.8);
            if (type == GrenadeType.MOLOTOV) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0f, 1.0f);
                this.discard();
                return;
            }
        } else {
            motion = motion.add(0, -0.045, 0);
            motion = motion.scale(0.985);
        }
        this.setDeltaMovement(motion);

        if (!this.level().isClientSide()) {
            this.fuse--;
            if (this.fuse <= 0) {
                if (type == GrenadeType.SMOKE) {
                    if (this.getDeltaMovement().lengthSqr() < 0.01) {
                        activateSmoke();
                    }
                } else {
                    detonate();
                }
            }
        }
    }

    private void handleEntityCollision(EntityHitResult hit, Vec3 motion) {
        this.setPos(hit.getLocation());
        Vec3 normal = this.position().subtract(hit.getEntity().position()).normalize();
        if (normal.lengthSqr() == 0) normal = new Vec3(0, 1, 0);
        Vec3 reflected = motion.subtract(normal.scale(2 * motion.dot(normal))).scale(0.3);
        this.setDeltaMovement(reflected);
        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_HURT, SoundSource.PLAYERS, 0.5f, 1.2f);
        }
    }

    private void handleBlockCollision(BlockHitResult hit, Vec3 motion) {
        BlockPos blockPos = hit.getBlockPos();
        BlockState state = this.level().getBlockState(blockPos);

        boolean isGlass = state.is(BlockTags.IMPERMEABLE) || state.getBlock() instanceof StainedGlassBlock || state.getBlock() instanceof IronBarsBlock || state.getBlock().getDescriptionId().contains("glass");
        if (isGlass && motion.length() > 0.35) {
            if (!this.level().isClientSide()) {
                this.level().destroyBlock(blockPos, false, this.getOwner());
            }
            this.setPos(hit.getLocation());
            this.setDeltaMovement(motion.scale(0.75));
            return;
        }

        Direction dir = hit.getDirection();
        Vec3 normal = new Vec3(dir.getStepX(), dir.getStepY(), dir.getStepZ());
        this.setPos(hit.getLocation().add(normal.scale(0.05)));

        if (type.breakOnGround && dir == Direction.UP) {
            if (!this.level().isClientSide()) {
                detonate();
            }
            return;
        }

        double dot = motion.dot(normal);
        Vec3 reflection = motion.subtract(normal.scale(2.0 * dot));
        Vec3 normalComponent = normal.scale(reflection.dot(normal));
        Vec3 tangentComponent = reflection.subtract(normalComponent);

        Vec3 newMotion = normalComponent.scale(type.bounciness).add(tangentComponent.scale(type.friction));

        if (newMotion.length() < 0.08 && dir == Direction.UP) {
            newMotion = Vec3.ZERO;
        } else if (!this.level().isClientSide() && motion.length() > 0.15) {
            float pitch = 0.9f + this.random.nextFloat() * 0.3f;
            float volume = (float) Math.min(1.0, motion.length() * 0.8);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL, volume * 0.35f, pitch * 1.6f);
        }

        this.setDeltaMovement(newMotion);
    }

    private void activateSmoke() {
        this.entityData.set(TRIGGERED, true);
        this.activeTicks = 300;
        this.setDeltaMovement(Vec3.ZERO);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.5f, 0.7f);
    }

    private void handleTriggeredState() {
        if (this.level().isClientSide()) {
            for (int i = 0; i < 18; i++) {
                double rx = (this.random.nextDouble() - 0.5) * 6.5;
                double ry = this.random.nextDouble() * 3.8;
                double rz = (this.random.nextDouble() - 0.5) * 6.5;
                if (rx * rx + rz * rz <= 11.0) {
                    this.level().addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true, this.getX() + rx, this.getY() + ry, this.getZ() + rz, 0.0, 0.02, 0.0);
                }
            }
        } else {
            this.activeTicks--;
            if (this.activeTicks <= 0) {
                this.discard();
            }
        }
    }

    private void detonate() {
        if (this.level().isClientSide()) return;

        if (type == GrenadeType.FRAG) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.2f, false, Level.ExplosionInteraction.NONE);
            AABB area = this.getBoundingBox().inflate(7.5);
            for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, area)) {
                double dist = target.distanceTo(this);
                if (dist <= 7.5 && hasLineOfSight(target)) {
                    float dmg = (float) ((1.0 - (dist / 7.5)) * 26.0);
                    target.hurt(this.damageSources().explosion(this, this.getOwner()), Math.max(2.0f, dmg));
                }
            }
        } else if (type == GrenadeType.MOLOTOV) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.5f, 0.8f);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 1.5f, 1.0f);
            BlockPos center = this.blockPosition();
            for (int x = -3; x <= 3; x++) {
                for (int y = -1; y <= 2; y++) {
                    for (int z = -3; z <= 3; z++) {
                        if (x * x + z * z <= 10) {
                            BlockPos pos = center.offset(x, y, z);
                            if (this.level().isEmptyBlock(pos) && BaseFireBlock.canBePlacedAt(this.level(), pos, Direction.UP)) {
                                this.level().setBlockAndUpdate(pos, BaseFireBlock.getState(this.level(), pos));
                            }
                        }
                    }
                }
            }
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.LAVA, this.getX(), this.getY() + 0.3, this.getZ(), 25, 1.5, 0.3, 1.5, 0.1);
            }
        } else if (type == GrenadeType.FLASHBANG) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.NEUTRAL, 2.5f, 1.9f);
            if (this.level() instanceof ServerLevel serverLevel) {
//                serverLevel.sendParticles(ParticleTypes.FLASH., this.getX(), this.getY() + 0.2, this.getZ(), 2, 0, 0, 0, 0);
            }
            AABB area = this.getBoundingBox().inflate(18.0);
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity target : entities) {
                double dist = target.distanceTo(this);
                if (dist <= 18.0 && hasLineOfSight(target)) {
                    Vec3 lookVec = target.getLookAngle().normalize();
                    Vec3 toGrenade = this.position().subtract(target.getEyePosition()).normalize();
                    double dot = lookVec.dot(toGrenade);
                    double distFactor = 1.0 - (dist / 18.0);
                    int blindDuration;
                    if (dot > 0.3) {
                        blindDuration = (int) (120 * distFactor * ((dot + 1.0) / 2.0));
                    } else {
                        blindDuration = (int) (35 * distFactor);
                    }
                    if (blindDuration > 5) {
                        target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, blindDuration, 1, false, false));
                        target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, blindDuration / 2, 1, false, false));
                    }
                }
            }
        }
        this.discard();
    }

    private boolean hasLineOfSight(Entity target) {
        Vec3 start = this.position().add(0, 0.2, 0);
        Vec3 end = target.getEyePosition();
        BlockHitResult hit = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.of(this)));
        return hit.getType() == HitResult.Type.MISS;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity);
    }
}