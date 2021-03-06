package sophisticated_wolves.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Sophisticated Wolves
 *
 * @author metroidfood
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class EntityAIAvoidFire extends EntityAIBase {
    private EntityCreature entity;
    private double xPath;
    private double yPath;
    private double zPath;
    private double moveSpeed;
    private double minSpeed;
    private double maxSpeed;
    private World theWorld;

    public EntityAIAvoidFire(EntityCreature entity, double minSpeed, double maxSpeed) {
        this.entity = entity;
        this.moveSpeed = this.entity.getMoveHelper().getSpeed();
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.theWorld = this.entity.world;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (!this.entity.onGround) {
            return false;
        }

        Vec3d vec3d = RandomPositionGenerator.findRandomTarget(entity, 10, 7);

        if (vec3d == null) {
            return false;
        } else {
            this.xPath = vec3d.x;
            this.yPath = vec3d.y;
            this.zPath = vec3d.z;
            return this.theWorld.isFlammableWithin(this.entity.getEntityBoundingBox().contract(0.001, 0.001, 0.001));
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPath, this.yPath, this.zPath, this.moveSpeed);
    }

    @Override
    public void updateTask() {
        if (this.entity.isBurning()) {
            this.entity.getNavigator().setSpeed(this.maxSpeed);
        } else {
            this.entity.getNavigator().setSpeed(this.minSpeed);
        }
    }
}
