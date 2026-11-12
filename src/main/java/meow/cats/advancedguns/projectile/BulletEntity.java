package meow.cats.advancedguns.projectile;

import meow.cats.advancedguns.network.LagCompensator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import java.util.Optional;
import static meow.cats.advancedguns.items.ModItems.AMMO;

public class BulletEntity extends Projectile implements ItemSupplier {
    private final float baseDamage;
    private final float maxSpeed;
    private final float gravity;
    private final float airResistance;
    private Item ammo = AMMO;
    private boolean explode;
    private int shooterPingTicks = 0;
    private static final EntityDataAccessor<ItemStack> CUSTOM_DATA_ITEM_STACK = SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.ITEM_STACK);

    public BulletEntity(EntityType<? extends BulletEntity> type, Level level) {
        super(type, level);
        this.baseDamage = 6.0f;
        this.maxSpeed = 15.0f;
        this.gravity = 0.0245f;
        this.airResistance = 0.99f;
    }

    public BulletEntity(Level level, LivingEntity owner, float speed, float damage, float gravity, float airResistance) {
        super(ModEntities.BULLET, level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        this.setDeltaMovement(owner.getLookAngle().scale(speed));
        this.baseDamage = damage;
        this.maxSpeed = speed;
        this.gravity = gravity;
        this.airResistance = airResistance;
        if (owner instanceof ServerPlayer serverPlayer) {
            this.shooterPingTicks = Math.min(20, Math.max(0, serverPlayer.connection.latency() / 50));
        }
    }

    public BulletEntity setAmmo(Item ammo) {
        this.ammo = ammo;
        this.entityData.set(CUSTOM_DATA_ITEM_STACK, new ItemStack(ammo));
        return this;
    }

    @Override
    public ItemStack getItem() {
        return this.entityData.get(CUSTOM_DATA_ITEM_STACK);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(CUSTOM_DATA_ITEM_STACK, new ItemStack(AMMO));
    }

    private HitResult getBulletHitResult(Vec3 startPos, Vec3 endPos) {
        BlockHitResult blockHit = this.level().clip(new ClipContext(
                startPos,
                endPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                CollisionContext.of(this)
        ) {
            @Override
            public net.minecraft.world.phys.shapes.VoxelShape getBlockShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
                if (state.is(BlockTags.LEAVES)) {
                    return Shapes.empty();
                }
                return super.getBlockShape(state, level, pos);
            }
        });

        Vec3 entityCheckEnd = blockHit.getType() != HitResult.Type.MISS ? blockHit.getLocation() : endPos;
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                this.level(),
                this,
                startPos,
                entityCheckEnd,
                this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0),
                this::canHitEntity
        );

        return entityHit != null ? entityHit : blockHit;
    }

    private EntityHitResult findCompensatedEntityHit(Vec3 startVec, Vec3 endVec) {
        Entity closestEntity = null;
        Vec3 closestHitPos = null;
        double closestDistSqr = Double.MAX_VALUE;
        long targetTick = this.level().getGameTime() - this.shooterPingTicks;
        AABB searchArea = this.getBoundingBox().expandTowards(endVec.subtract(startVec)).inflate(2.0);
        for (Entity target : this.level().getEntities(this, searchArea, this::canHitEntity)) {
            AABB targetBox = LagCompensator.getCompensatedBox(target, targetTick).inflate(0.1);
            Optional<Vec3> hitOpt = targetBox.clip(startVec, endVec);
            if (hitOpt.isPresent()) {
                double distSqr = startVec.distanceToSqr(hitOpt.get());
                if (distSqr < closestDistSqr) {
                    closestDistSqr = distSqr;
                    closestEntity = target;
                    closestHitPos = hitOpt.get();
                }
            }
        }
        return closestEntity != null ? new EntityHitResult(closestEntity, closestHitPos) : null;
    }

    @Override
    public void tick() {
        Vec3 startPos = this.position();
        Vec3 motion = this.getDeltaMovement();
        Vec3 endPos = startPos.add(motion);

        HitResult hitResult = getBulletHitResult(startPos, endPos);

        if (!this.level().isClientSide() && this.shooterPingTicks > 0) {
            Vec3 blockLimit = hitResult.getType() == HitResult.Type.BLOCK ? hitResult.getLocation() : endPos;
            EntityHitResult lagHit = findCompensatedEntityHit(startPos, blockLimit);
            if (lagHit != null) {
                hitResult = lagHit;
            }
        }

        if (hitResult.getType() != HitResult.Type.MISS) {
            endPos = hitResult.getLocation();
        }
        this.setPos(endPos);

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            float currentSpeed = (float) motion.length();
            float speedRatio = maxSpeed > 0 ? (currentSpeed / maxSpeed) : 0;
            float calculatedDamage = Math.max(1.0f, baseDamage * speedRatio);
            if (explode) {
                if (!this.level().isClientSide()) {
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 4.5f, false, Level.ExplosionInteraction.TNT);
                }
            }
            entityHitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), calculatedDamage);
            this.discard();
            return;
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = this.level().getBlockState(blockPos);
            if (!blockState.isAir()) {
                boolean isGlass = blockState.is(BlockTags.IMPERMEABLE) || blockState.getBlock() instanceof StainedGlassBlock || blockState.getBlock() instanceof IronBarsBlock || blockState.getBlock().getDescriptionId().contains("glass");
                if (isGlass && !this.explode) {
                    this.level().destroyBlock(blockPos, false, this.getOwner());
                    motion = motion.scale(0.85f);
                } else {
                    if (this.explode) {
                        if (!this.level().isClientSide()) {
                            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 4.5f, false, Level.ExplosionInteraction.TNT);
                        }
                    } else if (!this.level().isClientSide()) {
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.TUFF_BREAK, SoundSource.BLOCKS, 1.5f, 0.8f);
                    }
                    this.discard();
                    return;
                }
            }
        }
        motion = motion.add(0, -gravity, 0);
        motion = motion.scale(airResistance);
        this.setDeltaMovement(motion);
        if (this.level().isClientSide()) {
            this.level().addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        if (this.getY() < this.level().getMinY()) {
            this.discard();
        }
    }

    public BulletEntity shouldExplode(Boolean bool) {
        explode = bool;
        return this;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity);
    }
}
