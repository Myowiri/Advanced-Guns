package meow.cats.projectile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import static meow.cats.items.ModItems.AMMO;

public class BulletEntity extends Projectile implements ItemSupplier {
    private final float baseDamage;
    private final float maxSpeed;
    private final float gravity;
    private final float airResistance;

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
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
    }

    @Override
    public void tick() {
        Vec3 startPos = this.position();
        Vec3 motion = this.getDeltaMovement();
        Vec3 endPos = startPos.add(motion);

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS) {
            endPos = hitResult.getLocation();
        }

        this.setPos(endPos);

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;

            float currentSpeed = (float) motion.length();
            float speedRatio = maxSpeed > 0 ? (currentSpeed / maxSpeed) : 0;
            float calculatedDamage = Math.max(1.0f, baseDamage * speedRatio);

            entityHitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), calculatedDamage);
            this.discard();
            return;
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            if (this.level().getBlockState(blockHitResult.getBlockPos()).getBlock() != Blocks.AIR) {

                if (!this.level().isClientSide()) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.TUFF_BREAK, SoundSource.BLOCKS, 1.5f, 0.8f);
                }

                this.discard();
                return;
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

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(AMMO);
    }
}
