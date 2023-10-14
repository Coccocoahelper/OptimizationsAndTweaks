package fr.iamacat.multithreading.mixins.common.minefactoryreloaded;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.*;

import com.google.common.primitives.Ints;

import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.setup.MFRConfig;
import powercrystals.minefactoryreloaded.setup.MFRThings;
import powercrystals.minefactoryreloaded.world.MineFactoryReloadedWorldGen;
import powercrystals.minefactoryreloaded.world.WorldGenLakesMeta;
import powercrystals.minefactoryreloaded.world.WorldGenMassiveTree;
import powercrystals.minefactoryreloaded.world.WorldGenRubberTree;

@Mixin(MineFactoryReloadedWorldGen.class)
public class MixinFixCascadingforMineFactoryReloadedWorldGen {

    @Unique
    private static List<Integer> multithreadingandtweaks$_blacklistedDimensions;
    @Unique
    private static List<String> multithreadingandtweaks$_sludgeBiomeList;
    @Unique
    private static List<String> multithreadingandtweaks$_sewageBiomeList;
    @Unique
    private static List<String> multithreadingandtweaks$_rubberTreeBiomeList;
    @Unique
    private static boolean multithreadingandtweaks$_sludgeLakeMode;
    @Unique
    private static boolean multithreadingandtweaks$_sewageLakeMode;
    @Unique
    private static boolean multithreadingandtweaks$_rubberTreesEnabled;
    @Unique
    private static boolean multithreadingandtweaks$_lakesEnabled;
    @Unique
    private static boolean multithreadingandtweaks$_regenSewage;
    @Unique
    private static boolean multithreadingandtweaks$_regenSludge;
    @Unique
    private static boolean multithreadingandtweaks$_regenTrees;
    @Unique
    private static int multithreadingandtweaks$_sludgeLakeRarity;
    @Unique
    private static int multithreadingandtweaks$_sewageLakeRarity;

    public MixinFixCascadingforMineFactoryReloadedWorldGen() {}

    @Unique
    private static boolean multithreadingandtweaks$generateMegaRubberTree(World var0, Random var1, int var2, int var3,
        int var4, boolean var5) {
        return (new WorldGenMassiveTree(false)).setTreeScale((float) (4 + var1.nextInt(3)), 0.8F, 0.7F)
            .setLeafAttenuation(0.6F)
            .setSloped(true)
            .setSafe(var5)
            .func_76484_a(var0, var1, var2, var3, var4);
    }

    @Unique
    private static boolean multithreadingandtweaks$generateSacredSpringRubberTree(World var0, Random var1, int var2,
        int var3, int var4) {
        return (new WorldGenMassiveTree(false)).setTreeScale((float) (6 + var1.nextInt(4)), 1.0F, 0.9F)
            .setLeafAttenuation(0.35F)
            .setSloped(false)
            .setMinTrunkSize(4)
            .func_76484_a(var0, var1, var2, var3, var4);
    }

    @Shadow
    public String getFeatureName() {
        return "MFR:WorldGen";
    }

    /**
     * @author f
     * @reason f
     */

    @Overwrite(remap = false)
    public boolean generateFeature(Random var1, int var2, int var3, World var4, boolean var5) {
        if (multithreadingandtweaks$_blacklistedDimensions == null) {
            multithreadingandtweaks$buildBlacklistedDimensions();
        }

        if (multithreadingandtweaks$_blacklistedDimensions.contains(var4.provider.dimensionId)) {
            return false;
        }

        int var6 = var2 * 16 + var1.nextInt(16);
        int var7 = var3 * 16 + var1.nextInt(16);
        BiomeGenBase var8 = var4.getBiomeGenForCoords(var6, var7);

        if (var8 == null) {
            return false;
        }

        String var9 = var8.biomeName;

        // Generate rubber trees
        if (multithreadingandtweaks$_rubberTreesEnabled && (var5 || multithreadingandtweaks$_regenTrees)
            && multithreadingandtweaks$_rubberTreeBiomeList.contains(var9)
            && var1.nextInt(100) < 40) {
            if (var1.nextInt(30) == 0) {
                String var10 = var9.toLowerCase(Locale.US);
                if (var10.contains("mega")) {
                    multithreadingandtweaks$generateMegaRubberTree(
                        var4,
                        var1,
                        var6,
                        var4.getTopSolidOrLiquidBlock(var6, var7),
                        var7,
                        false);
                } else if (var10.contains("sacred") && var1.nextInt(20) == 0) {
                    multithreadingandtweaks$generateSacredSpringRubberTree(
                        var4,
                        var1,
                        var6,
                        var4.getTopSolidOrLiquidBlock(var6, var7),
                        var7);
                }
            }

            (new WorldGenRubberTree(false)).func_76484_a(var4, var1, var6, var1.nextInt(3) + 4, var7);
        }

        // Generate lakes
        if (multithreadingandtweaks$_lakesEnabled && var4.getWorldInfo()
            .isMapFeaturesEnabled()) {
            int var15;

            // Sludge lakes
            var15 = multithreadingandtweaks$_sludgeLakeRarity;
            if (var15 > 0 && (var5 || multithreadingandtweaks$_regenSludge)
                && multithreadingandtweaks$_sludgeBiomeList.contains(var9) == multithreadingandtweaks$_sludgeLakeMode
                && var1.nextInt(var15) == 0) {
                int var11 = var6 - 8 + var1.nextInt(16);
                int var12 = var1.nextInt(var4.getHeight());
                int var13 = var7 - 8 + var1.nextInt(16);
                (new WorldGenLakesMeta(MFRThings.sludgeLiquid, 0)).func_76484_a(var4, var1, var11, var12, var13);
            }

            // Sewage lakes
            var15 = multithreadingandtweaks$_sewageLakeRarity;
            if (var15 > 0 && (var5 || multithreadingandtweaks$_regenSewage)
                && multithreadingandtweaks$_sewageBiomeList.contains(var9) == multithreadingandtweaks$_sewageLakeMode
                && var1.nextInt(var15) == 0) {
                int var11 = var6 - 8 + var1.nextInt(16);
                int var12 = var1.nextInt(var4.getHeight());
                int var13 = var7 - 8 + var1.nextInt(16);
                String var14 = var9.toLowerCase(Locale.US);
                if (var14.contains("mushroom")) {
                    (new WorldGenLakesMeta(MFRThings.mushroomSoupLiquid, 0))
                        .func_76484_a(var4, var1, var11, var12, var13);
                } else {
                    (new WorldGenLakesMeta(MFRThings.sewageLiquid, 0)).func_76484_a(var4, var1, var11, var12, var13);
                }
            }
        }

        return true;
    }

    @Unique
    private static void multithreadingandtweaks$buildBlacklistedDimensions() {
        multithreadingandtweaks$_blacklistedDimensions = Ints.asList(MFRConfig.worldGenDimensionBlacklist.getIntList());
        multithreadingandtweaks$_rubberTreeBiomeList = MFRRegistry.getRubberTreeBiomes();
        multithreadingandtweaks$_rubberTreesEnabled = MFRConfig.rubberTreeWorldGen.getBoolean(true);
        multithreadingandtweaks$_lakesEnabled = MFRConfig.mfrLakeWorldGen.getBoolean(true);
        multithreadingandtweaks$_sludgeLakeRarity = MFRConfig.mfrLakeSludgeRarity.getInt();
        multithreadingandtweaks$_sludgeBiomeList = Arrays.asList(MFRConfig.mfrLakeSludgeBiomeList.getStringList());
        multithreadingandtweaks$_sludgeLakeMode = MFRConfig.mfrLakeSludgeBiomeListToggle.getBoolean(false);
        multithreadingandtweaks$_sewageLakeRarity = MFRConfig.mfrLakeSewageRarity.getInt();
        multithreadingandtweaks$_sewageBiomeList = Arrays.asList(MFRConfig.mfrLakeSewageBiomeList.getStringList());
        multithreadingandtweaks$_sewageLakeMode = MFRConfig.mfrLakeSewageBiomeListToggle.getBoolean(false);
        multithreadingandtweaks$_regenSewage = MFRConfig.mfrLakeSewageRetrogen.getBoolean(false);
        multithreadingandtweaks$_regenSludge = MFRConfig.mfrLakeSludgeRetrogen.getBoolean(false);
        multithreadingandtweaks$_regenTrees = MFRConfig.rubberTreeRetrogen.getBoolean(false);
    }
}
