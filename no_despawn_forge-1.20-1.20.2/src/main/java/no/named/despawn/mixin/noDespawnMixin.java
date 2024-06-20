package no.named.despawn.mixin;


import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(Mob.class)
public abstract class noDespawnMixin extends LivingEntity {


	protected noDespawnMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract boolean shouldDespawnInPeaceful();
    @Shadow
    public abstract boolean isPersistenceRequired();
    @Shadow
    public abstract boolean requiresCustomPersistence();
    @Shadow
    public abstract boolean removeWhenFarAway(double distanceSquared);

    public void checkDespawn(){
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful() && !this.isPersistenceRequired()) {
            this.discard();
		}
		else if (!this.isPersistenceRequired() && !this.requiresCustomPersistence()) {
			Entity entity = this.level().getNearestPlayer(this, -1.0D);
			if (entity != null) {
				double d0 = entity.distanceToSqr(this);
				int i = this.getType().getCategory().getDespawnDistance();
				int j = i * i;
				if (d0 > (double)j && this.removeWhenFarAway(d0)) {
					this.discard();
				}
				int k = this.getType().getCategory().getNoDespawnDistance();
				int l = k * k;
				if (this.noActionTime > 600 && this.random.nextInt(800) == 0 && d0 > (double)l && this.removeWhenFarAway(d0)) {
					this.discard();
				} 
				else if (d0 < (double)l) {
					this.noActionTime = 0;
				}
			}
		}
		else {
			this.noActionTime = 0;
		}
    }
}