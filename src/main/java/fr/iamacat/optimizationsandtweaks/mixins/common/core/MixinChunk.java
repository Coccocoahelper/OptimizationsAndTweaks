package fr.iamacat.optimizationsandtweaks.mixins.common.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Chunk.class)
public class MixinChunk {
    @Shadow
    private static final Logger logger = LogManager.getLogger();
    /** Determines if the chunk is lit or not at a light value greater than 0. */
    @Shadow
    public static boolean isLit;
    /**
     * Used to store block IDs, block MSBs, Sky-light maps, Block-light maps, and metadata. Each entry corresponds to a
     * logical segment of 16x16x16 blocks, stacked vertically.
     */
    @Shadow
    private ExtendedBlockStorage[] storageArrays;
    /** Contains a 16x16 mapping on the X/Z plane of the biome ID to which each colum belongs. */
    @Shadow
    private byte[] blockBiomeArray;
    /** A map, similar to heightMap, that tracks how far down precipitation can fall. */
    @Shadow
    public int[] precipitationHeightMap;
    /** Which columns need their skylightMaps updated. */
    @Shadow
    public boolean[] updateSkylightColumns;
    /** Whether or not this Chunk is currently loaded into the World */
    @Shadow
    public boolean isChunkLoaded;
    /** Reference to the World object. */
    @Shadow
    public World worldObj;
    public int[] heightMap;
    /** The x coordinate of the chunk. */
    @Shadow
    public final int xPosition;
    /** The z coordinate of the chunk. */
    @Shadow
    public final int zPosition;
    @Shadow
    private boolean isGapLightingUpdated;
    /** A Map of ChunkPositions to TileEntities in this chunk */
    @Shadow
    public Map chunkTileEntityMap;
    /** Array of Lists containing the entities in this Chunk. Each List represents a 16 block subchunk. */
    @Shadow
    public List[] entityLists;
    /** Boolean value indicating if the terrain is populated. */
    @Shadow
    public boolean isTerrainPopulated;
    @Shadow
    public boolean isLightPopulated;
    @Shadow
    public boolean field_150815_m;
    /** Set to true if the chunk has been modified and needs to be updated internally. */
    @Shadow
    public boolean isModified;
    /** Whether this Chunk has any Entities and thus requires saving on every tick */
    @Shadow
    public boolean hasEntities;
    /** The time according to World.worldTime when this chunk was last saved */
    @Shadow
    public long lastSaveTime;
    /**
     * Updates to this chunk will not be sent to clients if this is false. This field is set to true the first time the
     * chunk is sent to a client, and never set to false.
     */
    @Shadow
    public boolean sendUpdates;

    /** Lowest value in the heightmap. */
    @Shadow
    public int heightMapMinimum;
    /** the cumulative number of ticks players have been in this chunk */
    @Shadow
    public long inhabitedTime;
    /** Contains the current round-robin relight check index, and is implied as the relight check location as well. */
    @Shadow
    private int queuedLightChecks;

    public MixinChunk(int xPosition, int zPosition) {
        this.xPosition = xPosition;
        this.zPosition = zPosition;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getTopFilledSegment() {
        int low = 0;
        int high = this.storageArrays.length - 1;
        int topFilledSegment = -1;

        while (low <= high) {
            int mid = (low + high) >>> 1;

            if (this.storageArrays[mid] != null && this.storageArrays[mid].getYLocation() != -1) {
                topFilledSegment = this.storageArrays[mid].getYLocation();
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return topFilledSegment;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void getEntitiesWithinAABBForEntity(Entity entity, AxisAlignedBB aabb, List<Entity> listToFill,
        IEntitySelector entitySelector) {
        int minY = MathHelper.floor_double((aabb.minY - World.MAX_ENTITY_RADIUS) / 16.0D);
        int maxY = MathHelper.floor_double((aabb.maxY + World.MAX_ENTITY_RADIUS) / 16.0D);
        minY = MathHelper.clamp_int(minY, 0, this.entityLists.length - 1);
        maxY = MathHelper.clamp_int(maxY, 0, this.entityLists.length - 1);

        for (int y = minY; y <= maxY; ++y) {
            for (Object entityObj : this.entityLists[y]) {
                Entity targetEntity = (Entity) entityObj;

                if (optimizationsAndTweaks$isTargetEntityValid(entity, targetEntity, aabb, entitySelector)) {
                    listToFill.add(targetEntity);
                    optimizationsAndTweaks$addPartsIfValid(entity, aabb, entitySelector, listToFill, targetEntity);
                }
            }
        }
    }

    @Unique
    private boolean optimizationsAndTweaks$isTargetEntityValid(Entity sourceEntity, Entity targetEntity,
                                                               AxisAlignedBB aabb, IEntitySelector entitySelector) {
        return targetEntity != sourceEntity && targetEntity.boundingBox.intersectsWith(aabb)
            && (entitySelector == null || entitySelector.isEntityApplicable(targetEntity));
    }
    @Unique
    private void optimizationsAndTweaks$addPartsIfValid(Entity sourceEntity, AxisAlignedBB aabb,
        IEntitySelector entitySelector, List<Entity> listToFill, Entity targetEntity) {
        Entity[] parts = targetEntity.getParts();
        if (parts != null) {
            for (Entity partEntity : parts) {
                if (optimizationsAndTweaks$isTargetEntityValid(sourceEntity, partEntity, aabb, entitySelector)) {
                    listToFill.add(partEntity);
                }
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void getEntitiesOfTypeWithinAAAB(Class entityClass, AxisAlignedBB aabb, List listToFill,
        IEntitySelector entitySelector) {
        int minY = MathHelper.floor_double((aabb.minY - World.MAX_ENTITY_RADIUS) / 16.0D);
        int maxY = MathHelper.floor_double((aabb.maxY + World.MAX_ENTITY_RADIUS) / 16.0D);
        minY = MathHelper.clamp_int(minY, 0, this.entityLists.length - 1);
        maxY = MathHelper.clamp_int(maxY, 0, this.entityLists.length - 1);

        for (int y = minY; y <= maxY; ++y) {
            for (Object entityObj : this.entityLists[y]) {
                Entity entity = (Entity) entityObj;

                if (entityClass.isAssignableFrom(entity.getClass()) && entity.boundingBox.intersectsWith(aabb)
                    && (entitySelector == null || entitySelector.isEntityApplicable(entity))) {
                    listToFill.add(entity);
                }
            }
        }
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void func_150809_p() {
        isTerrainPopulated = true;
        isLightPopulated = true;

        if (worldObj.provider.hasNoSky || !worldObj.checkChunksExist(xPosition * 16 - 1, 0, zPosition * 16 - 1, xPosition * 16 + 1, 63, zPosition * 16 + 1)) {
            isLightPopulated = false;
            return;
        }

        for (int i = 0; i < 16 && isLightPopulated; ++i) {
            for (int j = 0; j < 16 && isLightPopulated; ++j) {
                isLightPopulated = func_150811_f(i, j);
            }
        }

        if (isLightPopulated) {
            optimizationsAndTweaks$updateChunk(3, -1, 0);
            optimizationsAndTweaks$updateChunk(1, 16, 0);
            optimizationsAndTweaks$updateChunk(0, 0, -1);
            optimizationsAndTweaks$updateChunk(2, 0, 16);
        }
    }

    @Unique
    private void optimizationsAndTweaks$updateChunk(int direction, int xOffset, int zOffset) {
        worldObj.getChunkFromBlockCoords(xPosition * 16 + xOffset, zPosition * 16 + zOffset);
        func_150801_a(direction);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    private void func_150801_a(int direction) {
        if (!isTerrainPopulated) {
            return;
        }

        for (int j = 0; j < 16; ++j) {
            switch (direction) {
                case 0:
                    func_150811_f(j, 15);
                    break;
                case 1:
                    func_150811_f(0, j);
                    break;
                case 2:
                    func_150811_f(j, 0);
                    break;
                case 3:
                    func_150811_f(15, j);
                    break;
            }
        }
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    private boolean func_150811_f(int x, int z) {
        int topFilledSegment = getTopFilledSegment();
        boolean flag = false;
        boolean flag1 = false;

        for (int y = topFilledSegment + 16 - 1; (y > 63 || (y > 0 && !flag1)); --y) {
            int lightValue = func_150808_b(x, y, z);

            if (lightValue == 255 && y < 63) {
                flag1 = true;
            }

            if (!flag && lightValue > 0) {
                flag = true;
            } else if (flag && lightValue == 0 && !worldObj.func_147451_t(xPosition * 16 + x, y, zPosition * 16 + z)) {
                return false;
            }
        }

        for (int y = topFilledSegment + 16 - 1; y > 0; --y) {
            if (getBlock(x, y, z).getLightValue() > 0) {
                worldObj.func_147451_t(xPosition * 16 + x, y, zPosition * 16 + z);
            }
        }

        return true;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public Block getBlock(final int p_150810_1_, final int p_150810_2_, final int p_150810_3_) {
        Block block = Blocks.air;

        if (p_150810_2_ >> 4 < this.storageArrays.length) {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[p_150810_2_ >> 4];

            if (extendedblockstorage != null) {
                block = extendedblockstorage.getBlockByExtId(p_150810_1_, p_150810_2_ & 15, p_150810_3_);
            }
        }

        return block;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public int func_150808_b(int p_150808_1_, int p_150808_2_, int p_150808_3_)
    {
        int x = (xPosition << 4) + p_150808_1_;
        int z = (zPosition << 4) + p_150808_3_;
        return this.getBlock(p_150808_1_, p_150808_2_, p_150808_3_).getLightOpacity(worldObj, x, p_150808_2_, z);
    }
}
