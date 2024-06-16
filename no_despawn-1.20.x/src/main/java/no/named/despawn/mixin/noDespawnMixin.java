package no.named.despawn.mixin;




import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
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

    public void checkDespawn() {
        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful() && !this.isPersistent()) {
            this.discard();
            return;
        }
		
        if (this.isPersistent() || this.cannotDespawn()) {
            this.despawnCounter = 0;
            return;
        }
		
        PlayerEntity entity = this.getWorld().getClosestPlayer(this, -1.0);
        
		if (entity != null) {
            int i;
            int j;
            double d = entity.squaredDistanceTo(this);
            if (d > (double)(j = (i = this.getType().getSpawnGroup().getImmediateDespawnRange()) * i) && this.canImmediatelyDespawn(d)) {
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
    }
}