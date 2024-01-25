package fr.iamacat.optimizationsandtweaks.utils.optimizationsandtweaks.vanilla;

import cpw.mods.fml.common.eventhandler.Event;
import fr.iamacat.optimizationsandtweaks.utils.agrona.collections.Object2ObjectHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Unique;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.world.SpawnerAnimals.canCreatureTypeSpawnAtLocation;

public class SpawnerAnimalsTask implements Runnable {
    public static final Object2ObjectHashMap optimizationsAndTweaks$eligibleChunksForSpawning = new Object2ObjectHashMap();
    private static final Thread countThread = new Thread(new SpawnerAnimalsTask(), "SpawnerAnimals-Thread");
    private static boolean threadStarted = false;

    private final EnumCreatureType creatureType;
    private final World world;
    private final Object2ObjectHashMap<ChunkCoordIntPair, Boolean> eligibleChunks;
    private final AtomicInteger result;

    private SpawnerAnimalsTask() {
        this.creatureType = null;
        this.world = null;
        this.eligibleChunks = null;
        this.result = null;
    }

    public SpawnerAnimalsTask(EnumCreatureType creatureType, World world, Object2ObjectHashMap<ChunkCoordIntPair,Boolean> eligibleChunks, AtomicInteger result) {
        this.creatureType = creatureType;
        this.world = world;
        this.eligibleChunks = eligibleChunks;
        this.result = result;
    }

    @Override
    public void run() {
        assert creatureType != null;
        assert eligibleChunks != null;
        int maxCreatureCount = optimizationsAndTweaks$getMaxCreatureCount(creatureType, eligibleChunks);
        assert world != null;

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int totalCreatureCount = forkJoinPool.invoke(new CreatureCountRecursiveTask(world.loadedEntityList.iterator(), creatureType, maxCreatureCount, eligibleChunks));

        assert result != null;
        result.set(totalCreatureCount);
    }

    private static int optimizationsAndTweaks$getMaxCreatureCount(EnumCreatureType creatureType, Object2ObjectHashMap<ChunkCoordIntPair, Boolean> eligibleChunks) {
        if (creatureType == null) {
            return 0;
        }
        return creatureType.getMaxNumberOfCreature() * eligibleChunks.size() / 256;
    }


    public int getTotalCreatureCount() {
        assert result != null;
        return result.get();
    }

    public static boolean optimizationsAndTweaks$shouldSpawnCreature(EnumCreatureType creatureType, World world, Object2ObjectHashMap<ChunkCoordIntPair,Boolean> eligibleChunks) {
        if (!threadStarted) {
            countThread.start();
            threadStarted = true;
        }

        int totalCreatureCount = new SpawnerAnimalsTask(creatureType, world, eligibleChunks, new AtomicInteger()).getTotalCreatureCount();
        int maxCreatureCount = optimizationsAndTweaks$getMaxCreatureCount(creatureType, eligibleChunks);
        return totalCreatureCount <= maxCreatureCount;
    }

    private static class CreatureCountRecursiveTask extends RecursiveTask<Integer> {
        private final Iterator<?> entityIterator;
        private final EnumCreatureType creatureType;
        private final int maxCreatureCount;
        private final Object2ObjectHashMap<ChunkCoordIntPair,Boolean> eligibleChunks;

        public CreatureCountRecursiveTask(Iterator<?> entityIterator, EnumCreatureType creatureType, int maxCreatureCount, Object2ObjectHashMap<ChunkCoordIntPair,Boolean> eligibleChunks) {
            this.entityIterator = entityIterator;
            this.creatureType = creatureType;
            this.maxCreatureCount = maxCreatureCount;
            this.eligibleChunks = eligibleChunks;
        }

        @Override
        protected Integer compute() {
            int totalCreatureCount = 0;
            Class<?> creatureClass = Objects.requireNonNull(creatureType.getCreatureClass());

            while (entityIterator.hasNext() && totalCreatureCount < maxCreatureCount) {
                Object entity = entityIterator.next();
                if (creatureClass.isInstance(entity) && isEntityInEligibleChunk((Entity) entity, eligibleChunks)) {
                    ++totalCreatureCount;
                }
            }

            return totalCreatureCount;
        }

        private boolean isEntityInEligibleChunk(Entity entity, Object2ObjectHashMap<ChunkCoordIntPair,Boolean> eligibleChunks) {
            double entityPosX = entity.posX;
            double entityPosZ = entity.posZ;
            int chunkX = MathHelper.floor_double(entityPosX) >> 4;
            int chunkZ = MathHelper.floor_double(entityPosZ) >> 4;
            ChunkCoordIntPair chunkCoord = new ChunkCoordIntPair(chunkX, chunkZ);
            Boolean isChunkEligible = eligibleChunks.get(chunkCoord);
            return isChunkEligible != null && isChunkEligible;
        }
    }
}