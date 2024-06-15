package no.named.despawn.mixin;




import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(MobEntity.class)
public abstract class noDespawnMixin extends LivingEntity {

    protected noDespawnMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract boolean isDisallowedInPeaceful();
    @Shadow
    public abstract boolean isPersistent();
    @Shadow
    public abstract boolean cannotDespawn();
    @Shadow
    public abstract boolean canImmediatelyDespawn(double distanceSquared);

    public void checkDespawn(){
        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful() && !this.isPersistent()) {
            this.discard();
        } else if (!this.isPersistent() && !this.cannotDespawn()) {
            Entity entity = this.world.getClosestPlayer(this, -1.0D);
            if (entity != null) {
                double d = entity.squaredDistanceTo((Entity)this);
                int i = this.getType().getSpawnGroup().getImmediateDespawnRange();
                int j = i * i;
                if (d > (double)j && this.canImmediatelyDespawn(d)) {
                    this.discard();
                }

                int k = this.getType().getSpawnGroup().getDespawnStartRange();
                int l = k * k;
                if (this.despawnCounter > 600 && this.random.nextInt(800) == 0 && d > (double)l && this.canImmediatelyDespawn(d)) {
                    this.discard();
                } else if (d < (double)l) {
                    this.despawnCounter = 0;
                }
            }

        } else {
            this.despawnCounter = 0;
        }
    }
}