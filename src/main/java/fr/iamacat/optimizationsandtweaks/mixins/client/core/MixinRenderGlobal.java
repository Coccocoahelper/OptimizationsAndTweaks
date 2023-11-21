package fr.iamacat.optimizationsandtweaks.mixins.client.core;

import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.util.RenderDistanceSorter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.IntBuffer;
import java.util.*;
@SideOnly(Side.CLIENT)
@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {
    @Shadow
    private static final Logger logger = LogManager.getLogger();
    @Shadow
    private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
    @Shadow
    private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
    @Shadow
    private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
    @Shadow
    private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
    @Shadow
    public List tileEntities = new ArrayList();
    @Shadow
    private WorldClient theWorld;
    /** The RenderEngine instance used by RenderGlobal */
    @Shadow
    private List worldRenderersToUpdate = new ArrayList();
    @Shadow
    private WorldRenderer[] sortedWorldRenderers;
    @Shadow
    private WorldRenderer[] worldRenderers;
    @Shadow
    private int renderChunksWide;
    @Shadow
    private int renderChunksTall;
    @Shadow
    private int renderChunksDeep;
    /** OpenGL render lists base */
    @Shadow
    private int glRenderListBase;
    /** A reference to the Minecraft object. */
    @Shadow
    private Minecraft mc;
    @Shadow
    private RenderBlocks renderBlocksRg;
    /** OpenGL occlusion query base */
    @Shadow
    private IntBuffer glOcclusionQueryBase;
    /** Is occlusion testing enabled */
    @Shadow
    private boolean occlusionEnabled;
    /** counts the cloud render updates. Used with mod to stagger some updates */
    @Shadow
    private int cloudTickCounter;
    /** The star GL Call list */
    @Shadow
    private int starGLCallList;
    /** OpenGL sky list */
    @Shadow
    private int glSkyList;
    /** OpenGL sky list 2 */
    @Shadow
    private int glSkyList2;
    /** Minimum block X */
    @Shadow
    private int minBlockX;
    /** Minimum block Y */
    @Shadow
    private int minBlockY;
    /** Minimum block Z */
    @Shadow
    private int minBlockZ;
    /** Maximum block X */
    @Shadow
    private int maxBlockX;
    /** Maximum block Y */
    @Shadow
    private int maxBlockY;
    /** Maximum block Z */
    @Shadow
    private int maxBlockZ;
    /**
     * Stores blocks currently being broken. Key is entity ID of the thing doing the breaking. Value is a
     * DestroyBlockProgress
     */
    @Shadow
    private final Map damagedBlocks = new HashMap();
    /** Currently playing sounds.  Type:  HashMap<ChunkCoordinates, ISound> */
    @Shadow
    private final Map mapSoundPositions = Maps.newHashMap();
    @Shadow
    private IIcon[] destroyBlockIcons;
    @Shadow
    private boolean displayListEntitiesDirty;
    @Shadow
    private int displayListEntities;
    @Shadow
    private int renderDistanceChunks = -1;
    /** Render entities startup counter (init value=2) */
    @Shadow
    private int renderEntitiesStartupCounter = 2;
    /** Count entities total */
    @Shadow
    private int countEntitiesTotal;
    /** Count entities rendered */
    @Shadow
    private int countEntitiesRendered;
    /** Count entities hidden */
    @Shadow
    private int countEntitiesHidden;
    /** Occlusion query result */
    @Shadow
    IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);
    /** How many renderers are loaded this frame that try to be rendered */
    @Shadow
    private int renderersLoaded;
    /** How many renderers are being clipped by the frustrum this frame */
    @Shadow
    private int renderersBeingClipped;
    /** How many renderers are being occluded this frame */
    @Shadow
    private int renderersBeingOccluded;
    /** How many renderers are actually being rendered this frame */
    @Shadow
    private int renderersBeingRendered;
    /** How many renderers are skipping rendering due to not having a render pass this frame */
    @Shadow
    private int renderersSkippingRenderPass;
    /** Dummy render int */
    @Shadow
    private int dummyRenderInt;
    /** World renderers check index */
    @Shadow
    private int worldRenderersCheckIndex;
    /** List of OpenGL lists for the current render pass */
    @Shadow
    private List glRenderLists = new ArrayList();
    /** All render lists (fixed length 4) */
    @Shadow
    private RenderList[] allRenderLists = new RenderList[] {new RenderList(), new RenderList(), new RenderList(), new RenderList()};
    /**
     * Previous x position when the renderers were sorted. (Once the distance moves more than 4 units they will be
     * resorted)
     */
    @Shadow
    double prevSortX = -9999.0D;
    /**
     * Previous y position when the renderers were sorted. (Once the distance moves more than 4 units they will be
     * resorted)
     */
    @Shadow
    double prevSortY = -9999.0D;
    /**
     * Previous Z position when the renderers were sorted. (Once the distance moves more than 4 units they will be
     * resorted)
     */
    @Shadow
    double prevSortZ = -9999.0D;
    @Shadow
    double prevRenderSortX = -9999.0D;
    @Shadow
    double prevRenderSortY = -9999.0D;
    @Shadow
    double prevRenderSortZ = -9999.0D;
    @Shadow
    int prevChunkSortX = -999;
    @Shadow
    int prevChunkSortY = -999;
    @Shadow
    int prevChunkSortZ = -999;
    /** The offset used to determine if a renderer is one of the sixteenth that are being updated this frame */
    @Shadow
    int frustumCheckOffset;
    /**
     * @author
     * @reason
     */
    @Overwrite
    private int renderSortedRenderers(int p_72724_1_, int p_72724_2_, int p_72724_3_, double p_72724_4_) {
        this.glRenderLists.clear();
        int l = 0;
        int i1 = p_72724_1_;
        int j1 = p_72724_2_;
        byte b0 = 1;

        if (p_72724_3_ == 1) {
            i1 = this.sortedWorldRenderers.length - 1 - p_72724_1_;
            j1 = this.sortedWorldRenderers.length - 1 - p_72724_2_;
            b0 = -1;
        }

        WorldRenderer[] sortedRenderers = this.sortedWorldRenderers;

        for (int k1 = i1; k1 != j1; k1 += b0) {
            WorldRenderer currentRenderer = sortedRenderers[k1];

            boolean isPassZero = p_72724_3_ == 0;
            boolean skipPass = currentRenderer.skipRenderPass[p_72724_3_];
            boolean isInFrustum = currentRenderer.isInFrustum;
            boolean isOcclusionEnabled = this.occlusionEnabled && !currentRenderer.isVisible;

            if (isPassZero) {
                ++this.renderersLoaded;
                if (skipPass) {
                    ++this.renderersSkippingRenderPass;
                } else if (!isInFrustum) {
                    ++this.renderersBeingClipped;
                } else if (isOcclusionEnabled) {
                    ++this.renderersBeingOccluded;
                } else {
                    ++this.renderersBeingRendered;
                }
            }

            if (!skipPass && isInFrustum && (!this.occlusionEnabled || currentRenderer.isVisible)) {
                int glCallList = currentRenderer.getGLCallListForPass(p_72724_3_);
                if (glCallList >= 0) {
                    this.glRenderLists.add(currentRenderer);
                    ++l;
                }
            }
        }

        EntityLivingBase entitylivingbase = this.mc.renderViewEntity;
        double d3 = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * p_72724_4_;
        double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * p_72724_4_;
        double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * p_72724_4_;
        int i2 = 0;
        int j2;

        for (j2 = 0; j2 < this.allRenderLists.length; ++j2)
        {
            this.allRenderLists[j2].resetList();
        }

        int k2;
        int l2;

        for (j2 = 0; j2 < this.glRenderLists.size(); ++j2)
        {
            WorldRenderer worldrenderer = (WorldRenderer)this.glRenderLists.get(j2);
            k2 = -1;

            for (l2 = 0; l2 < i2; ++l2)
            {
                if (this.allRenderLists[l2].rendersChunk(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus))
                {
                    k2 = l2;
                }
            }

            if (k2 < 0)
            {
                k2 = i2++;
                this.allRenderLists[k2].setupRenderList(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus, d3, d1, d2);
            }

            this.allRenderLists[k2].addGLRenderList(worldrenderer.getGLCallListForPass(p_72724_3_));
        }

        j2 = MathHelper.floor_double(d3);
        int i3 = MathHelper.floor_double(d2);
        k2 = j2 - (j2 & 1023);
        l2 = i3 - (i3 & 1023);
        Arrays.sort(this.allRenderLists, new RenderDistanceSorter(k2, l2));
        this.renderAllRenderLists(p_72724_3_, p_72724_4_);
        return l;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void renderAllRenderLists(int p_72733_1_, double p_72733_2_)
    {
        this.mc.entityRenderer.enableLightmap(p_72733_2_);

        for (RenderList allRenderList : this.allRenderLists) {
            allRenderList.callLists();
        }

        this.mc.entityRenderer.disableLightmap(p_72733_2_);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public int sortAndRender(EntityLivingBase p_72719_1_, int p_72719_2_, double p_72719_3_)
    {
        this.theWorld.theProfiler.startSection("sortchunks");

        for (int j = 0; j < 10; ++j)
        {
            this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
            WorldRenderer worldrenderer = this.worldRenderers[this.worldRenderersCheckIndex];

            if (worldrenderer.needsUpdate && !this.worldRenderersToUpdate.contains(worldrenderer))
            {
                this.worldRenderersToUpdate.add(worldrenderer);
            }
        }

        if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks)
        {
            this.loadRenderers();
        }

        if (p_72719_2_ == 0)
        {
            this.renderersLoaded = 0;
            this.dummyRenderInt = 0;
            this.renderersBeingClipped = 0;
            this.renderersBeingOccluded = 0;
            this.renderersBeingRendered = 0;
            this.renderersSkippingRenderPass = 0;
        }

        double d9 = p_72719_1_.lastTickPosX + (p_72719_1_.posX - p_72719_1_.lastTickPosX) * p_72719_3_;
        double d1 = p_72719_1_.lastTickPosY + (p_72719_1_.posY - p_72719_1_.lastTickPosY) * p_72719_3_;
        double d2 = p_72719_1_.lastTickPosZ + (p_72719_1_.posZ - p_72719_1_.lastTickPosZ) * p_72719_3_;
        double d3 = p_72719_1_.posX - this.prevSortX;
        double d4 = p_72719_1_.posY - this.prevSortY;
        double d5 = p_72719_1_.posZ - this.prevSortZ;

        if (this.prevChunkSortX != p_72719_1_.chunkCoordX || this.prevChunkSortY != p_72719_1_.chunkCoordY || this.prevChunkSortZ != p_72719_1_.chunkCoordZ || d3 * d3 + d4 * d4 + d5 * d5 > 16.0D)
        {
            this.prevSortX = p_72719_1_.posX;
            this.prevSortY = p_72719_1_.posY;
            this.prevSortZ = p_72719_1_.posZ;
            this.prevChunkSortX = p_72719_1_.chunkCoordX;
            this.prevChunkSortY = p_72719_1_.chunkCoordY;
            this.prevChunkSortZ = p_72719_1_.chunkCoordZ;
            this.markRenderersForNewPosition(MathHelper.floor_double(p_72719_1_.posX), MathHelper.floor_double(p_72719_1_.posY), MathHelper.floor_double(p_72719_1_.posZ));
            Arrays.sort(this.sortedWorldRenderers, new EntitySorter(p_72719_1_));
        }

        double d6 = p_72719_1_.posX - this.prevRenderSortX;
        double d7 = p_72719_1_.posY - this.prevRenderSortY;
        double d8 = p_72719_1_.posZ - this.prevRenderSortZ;
        int k;

        if (d6 * d6 + d7 * d7 + d8 * d8 > 1.0D)
        {
            this.prevRenderSortX = p_72719_1_.posX;
            this.prevRenderSortY = p_72719_1_.posY;
            this.prevRenderSortZ = p_72719_1_.posZ;

            for (k = 0; k < 27; ++k)
            {
                this.sortedWorldRenderers[k].updateRendererSort(p_72719_1_);
            }
        }

        RenderHelper.disableStandardItemLighting();
        byte b1 = 0;

        if (this.occlusionEnabled && this.mc.gameSettings.advancedOpengl && !this.mc.gameSettings.anaglyph && p_72719_2_ == 0)
        {
            byte b0 = 0;
            int l = 16;
            this.checkOcclusionQueryResult(b0, l);

            for (int i1 = b0; i1 < l; ++i1)
            {
                this.sortedWorldRenderers[i1].isVisible = true;
            }

            this.theWorld.theProfiler.endStartSection("render");
            k = b1 + this.renderSortedRenderers(b0, l, p_72719_2_, p_72719_3_);

            do
            {
                this.theWorld.theProfiler.endStartSection("occ");
                int l1 = l;
                l *= 2;

                if (l > this.sortedWorldRenderers.length)
                {
                    l = this.sortedWorldRenderers.length;
                }

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_FOG);
                GL11.glColorMask(false, false, false, false);
                GL11.glDepthMask(false);
                this.theWorld.theProfiler.startSection("check");
                this.checkOcclusionQueryResult(l1, l);
                this.theWorld.theProfiler.endSection();
                GL11.glPushMatrix();
                float f9 = 0.0F;
                float f = 0.0F;
                float f1 = 0.0F;

                for (int j1 = l1; j1 < l; ++j1)
                {
                    if (this.sortedWorldRenderers[j1].skipAllRenderPasses())
                    {
                        this.sortedWorldRenderers[j1].isInFrustum = false;
                    }
                    else
                    {
                        if (!this.sortedWorldRenderers[j1].isInFrustum)
                        {
                            this.sortedWorldRenderers[j1].isVisible = true;
                        }

                        if (this.sortedWorldRenderers[j1].isInFrustum && !this.sortedWorldRenderers[j1].isWaitingOnOcclusionQuery)
                        {
                            float f2 = MathHelper.sqrt_float(this.sortedWorldRenderers[j1].distanceToEntitySquared(p_72719_1_));
                            int k1 = (int)(1.0F + f2 / 128.0F);

                            if (this.cloudTickCounter % k1 == j1 % k1)
                            {
                                WorldRenderer worldrenderer1 = this.sortedWorldRenderers[j1];
                                float f3 = (float)((double)worldrenderer1.posXMinus - d9);
                                float f4 = (float)((double)worldrenderer1.posYMinus - d1);
                                float f5 = (float)((double)worldrenderer1.posZMinus - d2);
                                float f6 = f3 - f9;
                                float f7 = f4 - f;
                                float f8 = f5 - f1;

                                if (f6 != 0.0F || f7 != 0.0F || f8 != 0.0F)
                                {
                                    GL11.glTranslatef(f6, f7, f8);
                                    f9 += f6;
                                    f += f7;
                                    f1 += f8;
                                }

                                this.theWorld.theProfiler.startSection("bb");
                                ARBOcclusionQuery.glBeginQueryARB(ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB, this.sortedWorldRenderers[j1].glOcclusionQuery);
                                this.sortedWorldRenderers[j1].callOcclusionQueryList();
                                ARBOcclusionQuery.glEndQueryARB(ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB);
                                this.theWorld.theProfiler.endSection();
                                this.sortedWorldRenderers[j1].isWaitingOnOcclusionQuery = true;
                            }
                        }
                    }
                }

                GL11.glPopMatrix();

                if (this.mc.gameSettings.anaglyph)
                {
                    if (EntityRenderer.anaglyphField == 0)
                    {
                        GL11.glColorMask(false, true, true, true);
                    }
                    else
                    {
                        GL11.glColorMask(true, false, false, true);
                    }
                }
                else
                {
                    GL11.glColorMask(true, true, true, true);
                }

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_FOG);
                this.theWorld.theProfiler.endStartSection("render");
                k += this.renderSortedRenderers(l1, l, p_72719_2_, p_72719_3_);
            }
            while (l < this.sortedWorldRenderers.length);
        }
        else
        {
            this.theWorld.theProfiler.endStartSection("render");
            k = b1 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, p_72719_2_, p_72719_3_);
        }

        this.theWorld.theProfiler.endSection();
        return k;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    private void checkOcclusionQueryResult(int p_72720_1_, int p_72720_2_)
    {
        for (int k = p_72720_1_; k < p_72720_2_; ++k)
        {
            if (this.sortedWorldRenderers[k].isWaitingOnOcclusionQuery)
            {
                this.occlusionResult.clear();
                ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[k].glOcclusionQuery, ARBOcclusionQuery.GL_QUERY_RESULT_AVAILABLE_ARB, this.occlusionResult);

                if (this.occlusionResult.get(0) != 0)
                {
                    this.sortedWorldRenderers[k].isWaitingOnOcclusionQuery = false;
                    this.occlusionResult.clear();
                    ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[k].glOcclusionQuery, ARBOcclusionQuery.GL_QUERY_RESULT_ARB, this.occlusionResult);
                    this.sortedWorldRenderers[k].isVisible = this.occlusionResult.get(0) != 0;
                }
            }
        }
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    private void markRenderersForNewPosition(int p_72722_1_, int p_72722_2_, int p_72722_3_)
    {
        p_72722_1_ -= 8;
        p_72722_2_ -= 8;
        p_72722_3_ -= 8;
        this.minBlockX = Integer.MAX_VALUE;
        this.minBlockY = Integer.MAX_VALUE;
        this.minBlockZ = Integer.MAX_VALUE;
        this.maxBlockX = Integer.MIN_VALUE;
        this.maxBlockY = Integer.MIN_VALUE;
        this.maxBlockZ = Integer.MIN_VALUE;
        int l = this.renderChunksWide * 16;
        int i1 = l / 2;

        for (int j1 = 0; j1 < this.renderChunksWide; ++j1)
        {
            int k1 = j1 * 16;
            int l1 = k1 + i1 - p_72722_1_;

            if (l1 < 0)
            {
                l1 -= l - 1;
            }

            l1 /= l;
            k1 -= l1 * l;

            if (k1 < this.minBlockX)
            {
                this.minBlockX = k1;
            }

            if (k1 > this.maxBlockX)
            {
                this.maxBlockX = k1;
            }

            for (int i2 = 0; i2 < this.renderChunksDeep; ++i2)
            {
                int j2 = i2 * 16;
                int k2 = j2 + i1 - p_72722_3_;

                if (k2 < 0)
                {
                    k2 -= l - 1;
                }

                k2 /= l;
                j2 -= k2 * l;

                if (j2 < this.minBlockZ)
                {
                    this.minBlockZ = j2;
                }

                if (j2 > this.maxBlockZ)
                {
                    this.maxBlockZ = j2;
                }

                for (int l2 = 0; l2 < this.renderChunksTall; ++l2)
                {
                    int i3 = l2 * 16;

                    if (i3 < this.minBlockY)
                    {
                        this.minBlockY = i3;
                    }

                    if (i3 > this.maxBlockY)
                    {
                        this.maxBlockY = i3;
                    }

                    WorldRenderer worldrenderer = this.worldRenderers[(i2 * this.renderChunksTall + l2) * this.renderChunksWide + j1];
                    boolean flag = worldrenderer.needsUpdate;
                    worldrenderer.setPosition(k1, i3, j2);

                    if (!flag && worldrenderer.needsUpdate)
                    {
                        this.worldRenderersToUpdate.add(worldrenderer);
                    }
                }
            }
        }
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void loadRenderers()
    {
        if (this.theWorld != null)
        {
            Blocks.leaves.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
            Blocks.leaves2.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
            this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
            int i;

            if (this.worldRenderers != null)
            {
                for (i = 0; i < this.worldRenderers.length; ++i)
                {
                    this.worldRenderers[i].stopRendering();
                }
            }

            i = this.renderDistanceChunks * 2 + 1;
            this.renderChunksWide = i;
            this.renderChunksTall = 16;
            this.renderChunksDeep = i;
            this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
            this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
            int j = 0;
            int k = 0;
            this.minBlockX = 0;
            this.minBlockY = 0;
            this.minBlockZ = 0;
            this.maxBlockX = this.renderChunksWide;
            this.maxBlockY = this.renderChunksTall;
            this.maxBlockZ = this.renderChunksDeep;
            int l;

            for (l = 0; l < this.worldRenderersToUpdate.size(); ++l)
            {
                ((WorldRenderer)this.worldRenderersToUpdate.get(l)).needsUpdate = false;
            }

            this.worldRenderersToUpdate.clear();
            this.tileEntities.clear();
            this.onStaticEntitiesChanged();

            for (l = 0; l < this.renderChunksWide; ++l)
            {
                for (int i1 = 0; i1 < this.renderChunksTall; ++i1)
                {
                    for (int j1 = 0; j1 < this.renderChunksDeep; ++j1)
                    {
                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l] = new WorldRenderer(this.theWorld, this.tileEntities, l * 16, i1 * 16, j1 * 16, this.glRenderListBase + j);

                        if (this.occlusionEnabled)
                        {
                            this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].glOcclusionQuery = this.glOcclusionQueryBase.get(k);
                        }

                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].isWaitingOnOcclusionQuery = false;
                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].isVisible = true;
                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].isInFrustum = true;
                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].chunkIndex = k++;
                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].markDirty();
                        this.sortedWorldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l] = this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l];
                        this.worldRenderersToUpdate.add(this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l]);
                        j += 3;
                    }
                }
            }

            if (this.theWorld != null)
            {
                EntityLivingBase entitylivingbase = this.mc.renderViewEntity;

                if (entitylivingbase != null)
                {
                    this.markRenderersForNewPosition(MathHelper.floor_double(entitylivingbase.posX), MathHelper.floor_double(entitylivingbase.posY), MathHelper.floor_double(entitylivingbase.posZ));
                    Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entitylivingbase));
                }
            }

            this.renderEntitiesStartupCounter = 2;
        }
    }
    @Shadow
    public void onStaticEntitiesChanged()
    {
        this.displayListEntitiesDirty = true;
    }
}
