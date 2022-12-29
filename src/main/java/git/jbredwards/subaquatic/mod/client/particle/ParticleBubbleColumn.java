package git.jbredwards.subaquatic.mod.client.particle;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.vecmath.Vector2d;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class ParticleBubbleColumn extends ParticleBubble
{
    @Nonnull
    protected final Vector2d origin;
    protected final double radius;
    protected int angle;

    public ParticleBubbleColumn(@Nonnull World world, double x, double y, double z, double hSpeed, double vSpeed, double radiusIn, int angleIn) {
        super(world, x, y, z, 0, 0, 0);
        //removes RNG from the particle motion.
        motionX = hSpeed;
        motionY = vSpeed;
        motionZ = hSpeed;

        origin = new Vector2d(x, z);
        radius = radiusIn;
        angle = angleIn;
        setMaxAge(50);

        //prevents graphical bug when this particle initially spawns
        setNewPos();
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        angle++;

        setNewPos();
        if(particleMaxAge-- < 0 || !shouldExist()) setExpired();
    }

    protected boolean shouldExist() {
        return FluidloggedUtils.getFluidState(world, new BlockPos(posX, posY + 0.2, posZ)).getMaterial() == Material.WATER;
    }

    protected void setNewPos() {
        final double x = radius * Math.sin(Math.toRadians(angle * motionX));
        final double z = radius * Math.cos(Math.toRadians(angle * motionZ));
        setPosition(origin.x + x, posY + motionY, origin.y + z);
    }
}
