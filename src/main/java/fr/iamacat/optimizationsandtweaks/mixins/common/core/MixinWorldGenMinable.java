package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldGenMinable.class)
public class MixinWorldGenMinable extends WorldGenerator {

    @Shadow
    private Block field_150519_a;
    /** The number of blocks to generate. */
    @Shadow
    private int numberOfBlocks;
    @Shadow
    private Block field_150518_c;
    @Shadow
    private int mineableBlockMeta;

    @Unique
    private static final int SIN_COS_PRECISION = 360;
    @Unique
    private static final float[] SIN_TABLE = new float[SIN_COS_PRECISION];
    @Unique
    private static final float[] COS_TABLE = new float[SIN_COS_PRECISION];

    static {
        for (int i = 0; i < SIN_COS_PRECISION; i++) {
            float angle = (float) Math.toRadians(i);
            SIN_TABLE[i] = MathHelper.sin(angle);
            COS_TABLE[i] = MathHelper.cos(angle);
        }
    }

    /**
     * @author iamacatfr
     * @reason reducing TPS lags during worldgen
     */
    @Overwrite
    public boolean generate(World world, Random random, int x, int y, int z) {
        float f = random.nextFloat() * (float) Math.PI;
        double d0 = x + 8 + SIN_TABLE[MathHelper.floor_float(f * SIN_COS_PRECISION) & (SIN_COS_PRECISION - 1)] * numberOfBlocks / 8.0f;
        double d1 = x + 8 - SIN_TABLE[MathHelper.floor_float(f * SIN_COS_PRECISION) & (SIN_COS_PRECISION - 1)] * numberOfBlocks / 8.0f;
        double d2 = z + 8 + COS_TABLE[MathHelper.floor_float(f * SIN_COS_PRECISION) & (SIN_COS_PRECISION - 1)] * numberOfBlocks / 8.0f;
        double d3 = z + 8 - COS_TABLE[MathHelper.floor_float(f * SIN_COS_PRECISION) & (SIN_COS_PRECISION - 1)] * numberOfBlocks / 8.0f;
        double d4 = y + random.nextInt(3) - 2;
        double d5 = y + random.nextInt(3) - 2;

        optimizationsAndTweaks$generateEllipse(world, random, d0, d1, d2, d3, d4, d5);

        return true;
    }

    @Unique
    private void optimizationsAndTweaks$generateEllipse(World world, Random random, double d0, double d1, double d2, double d3, double d4, double d5) {
        for (int l = 0; l <= numberOfBlocks; ++l) {
            double d6 = d0 + (d1 - d0) * l / numberOfBlocks;
            double d7 = d4 + (d5 - d4) * l / numberOfBlocks;
            double d8 = d2 + (d3 - d2) * l / numberOfBlocks;
            double d9 = random.nextDouble() * numberOfBlocks / 16.0D;
            double d10 = (SIN_TABLE[MathHelper.floor_float(l * (float) Math.PI / numberOfBlocks * SIN_COS_PRECISION) & (SIN_COS_PRECISION - 1)] + 1.0f) * d9 + 1.0D;
            double d11 = (SIN_TABLE[MathHelper.floor_float(l * (float) Math.PI / numberOfBlocks * SIN_COS_PRECISION) & (SIN_COS_PRECISION - 1)] + 1.0f) * d9 + 1.0D;

            optimizationsAndTweaks$generateEllipseLayer(world, d6, d7, d8, d10, d11);
        }
    }

    @Unique
    private void optimizationsAndTweaks$generateEllipseLayer(World world, double d6, double d7, double d8, double d10, double d11) {
        for (int k2 = MathHelper.floor_double(d6 - d10 / 2.0D); k2 <= MathHelper.floor_double(d6 + d10 / 2.0D); ++k2) {
            double d12 = (k2 + 0.5D - d6) / (d10 / 2.0D);

            for (int l2 = MathHelper.floor_double(d7 - d11 / 2.0D); l2 <= MathHelper.floor_double(d7 + d11 / 2.0D); ++l2) {
                double d13 = (l2 + 0.5D - d7) / (d11 / 2.0D);

                for (int i3 = MathHelper.floor_double(d8 - d10 / 2.0D); i3 <= MathHelper.floor_double(d8 + d10 / 2.0D); ++i3) {
                    double d14 = (i3 + 0.5D - d8) / (d10 / 2.0D);

                    if (optimizationsAndTweaks$isInsideEllipse(d12, d13, d14)) {
                        optimizationsAndTweaks$replaceBlockIfReplaceable(world, k2, l2, i3);
                    }
                }
            }
        }
    }

    @Unique
    private boolean optimizationsAndTweaks$isInsideEllipse(double d12, double d13, double d14) {
        return d12 * d12 + d13 * d13 + d14 * d14 < 1.0D;
    }

    @Unique
    private void optimizationsAndTweaks$replaceBlockIfReplaceable(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block.isReplaceableOreGen(world, x, y, z, field_150518_c)) {
            world.setBlock(x, y, z, field_150519_a, mineableBlockMeta, 2);
        }
    }
}
