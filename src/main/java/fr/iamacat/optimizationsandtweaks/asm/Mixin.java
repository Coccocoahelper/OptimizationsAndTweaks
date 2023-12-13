package fr.iamacat.optimizationsandtweaks.asm;

import java.util.*;
import java.util.function.Predicate;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;

import fr.iamacat.optimizationsandtweaks.config.OptimizationsandTweaksConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Mixin implements IMixin {

    // TWEAKING MIXINS

    common_core_MixinWorld(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinWorld,
        "core.MixinWorld"),
    common_lotrimprovements_MixinMain(Side.COMMON,
        require(TargetedMod.LORDOFTHERINGSFORK).and(m -> OptimizationsandTweaksConfig.enableMixinMain),
        "lotrimprovements.MixinMain"),
    common_lotr_MixinLOTRMod(Side.COMMON,
        avoid(TargetedMod.LORDOFTHERINGSFORK)
            .and(m -> OptimizationsandTweaksConfig.enableMixinAddConfigForLOTRBIOMEIDS),
        "lotr.MixinLOTRMod"),
    common_lotr_MixinLOTRBiome(Side.COMMON,
        avoid(TargetedMod.LORDOFTHERINGSFORK)
            .and(m -> OptimizationsandTweaksConfig.enableMixinAddConfigForLOTRBIOMEIDS),
        "lotr.MixinLOTRBiome"),
    common_lotr_MixinLOTRWorldProvider(Side.COMMON,
        avoid(TargetedMod.LORDOFTHERINGSFORK)
            .and(require(TargetedMod.ENDLESSIDS).and(m -> OptimizationsandTweaksConfig.MixinLOTRWorldProvider)),
        "lotr.MixinLOTRWorldProvider"),

    common_minestones_MixinItemMinestone(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinItemMinestone,
        "minestones.MixinItemMinestone"),

    common_minestones_MixinMSConfig(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinMinestoneSupportDecimalValue, "minestones.MixinMSConfig"),
    common_minestones_MixinMSEvents(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinMinestoneSupportDecimalValue, "minestones.MixinMSEvents"),

    // OPTIMIZATIONS MIXINS
    common_core_MixinDedicatedServer(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinDedicatedServer,
        "core.MixinDedicatedServer"),
    common_core_MixinFMLClientHandler(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinFMLClientHandler,
        "core.MixinFMLClientHandler"),
    common_core_MixinFMLServerHandler(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinFMLServerHandler,
        "core.MixinFMLServerHandler"),
    common_core_MixinMinecraft(Side.COMMON,
        avoid(TargetedMod.FALSETWEAKS).and(m -> OptimizationsandTweaksConfig.enableMixinMinecraft),
        "core.MixinMinecraft"),
    common_core_MixinMinecraftServerGui(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinMinecraftServerGui,
        "core.MixinMinecraftServerGui"),
    common_core_MixinSaveFormatOld(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinSaveFormatOld,
        "core.MixinSaveFormatOld"),
    common_core_MixinThreadedFileIOBase(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinThreadedFileIOBase,
        "core.MixinThreadedFileIOBase"),

    common_core_MixinVec3(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinVec3, "core.MixinVec3"),

    common_core_MixinEntityAINearestAttackableTarget(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinEntityAINearestAttackableTarget,
        "core.MixinEntityAINearestAttackableTarget"),
    common_core_MixinEntityList(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityList,
        "core.MixinEntityList"),
    /*
     * common_core_MixinNBTTagCompound(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinNBTTagCompound,
     * "core.MixinNBTTagCompound"),
     */
    common_core_MixinEntityArrowAttack(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityArrowAttack,
        "core.MixinEntityArrowAttack"),
    common_core_MixinEntityAITarget(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityAITarget,
        "core.MixinEntityAITarget"),
    common_core_MixinAxisAlignedBB(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinAxisAlignedBB,
        "core.MixinAxisAlignedBB"),
    common_core_MixinEntityAITempt(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityAITempt,
        "core.MixinEntityAITempt"),
    client_core_MixinGuiNewChat(Side.CLIENT, m -> OptimizationsandTweaksConfig.enableMixinGuiNewChat,
        "core.MixinGuiNewChat"),

    client_core_MixinGui(Side.CLIENT, m -> OptimizationsandTweaksConfig.enableMixinGui, "core.MixinGui"),
    client_core_MixinRenderItem(Side.CLIENT,
        avoid(TargetedMod.FASTCRAFT).and(m -> OptimizationsandTweaksConfig.enableMixinRenderItem),
        "core.MixinRenderItem"),
    client_core_MixinRenderGlobal(Side.CLIENT, avoid(TargetedMod.FASTCRAFT).and(avoid(TargetedMod.OPTIFINE))
        .and(m -> OptimizationsandTweaksConfig.enableMixinRenderGlobal), "core.MixinRenderGlobal"),
    common_easybreeding_MixinEntityAIEatDroppedFood(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinEntityAIEatDroppedFood,
        "easybreeding.MixinEntityAIEatDroppedFood"),
    common_core_MixinRandomPositionGenerator(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinRandomPositionGenerator, "core.MixinRandomPositionGenerator"),
    common_core_MixinEntityAIWander(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityAIWander,
        "core.MixinEntityAIWander"),
    common_core_MixinEntityAIPlay(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityAIPlay,
        "core.MixinEntityAIPlay"),
    common_core_MixinEntityAIAttackOnCollide(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinEntityAIAttackOnCollide, "core.MixinEntityAIAttackOnCollide"),

    client_core_MixinRenderManager(Side.CLIENT,
        avoid(TargetedMod.SKINPORT).and(m -> OptimizationsandTweaksConfig.enableMixinRenderManager),
        "core.MixinRenderManager"),
    common_core_MixinServersideAttributeMap(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinServersideAttributeMap, "core.MixinServersideAttributeMap"),
    common_core_MixinLowerStringMap(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinLowerStringMap,
        "core.MixinLowerStringMap"),
    common_core_MixinEntityLivingUpdate(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityLivingUpdate,
        "core.MixinEntityLivingUpdate"),
    common_core_MixinDataWatcher(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinDataWatcher,
        "core.MixinDataWatcher"),

    client_practicallogistics_MixinEventRegistry(Side.CLIENT,
        m -> OptimizationsandTweaksConfig.enableMixinEventRegistry, "practicallogistics.MixinEventRegistry"),
    common_core_MixinNibbleArray(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinNibbleArray,
        "core.MixinNibbleArray"),
    common_blocklings_MixinEntityBlockling(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinSteamcraftEventHandler, "blocklings.MixinEntityBlockling"),
    common_flaxbeardssteampower_MixinSteamcraftEventHandler(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinSteamcraftEventHandler,
        "flaxbeardssteampower.MixinSteamcraftEventHandler"),
    common_catwalks2_MixinCommonProxy(Side.COMMON,
        avoid(TargetedMod.CATWALK2OFFICIAL).and(m -> OptimizationsandTweaksConfig.enableMixinCommonProxyForCatWalks2),
        "catwalks2.MixinCommonProxy"),
    common_core_MixinOilTweakEventHandler(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinOilTweakEventHandler,
        "buildcraft.addon.oiltweaks.MixinOilTweakEventHandler"),
    common_core_MixinMinecraftServer(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinMinecraftServer,
        "core.MixinMinecraftServer"),

    common_core_pathfinding_MixinPathFinder(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinPathFinder,
        "core.pathfinding.MixinPathFinder"),
    common_core_MixinWorldServer(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinWorldServer,
        "core.MixinWorldServer"),
    common_core_MixinStatsComponent(Side.SERVER, m -> OptimizationsandTweaksConfig.enableMixinStatsComponent,
        "core.MixinStatsComponent"),
    common_core_MixinChunk(Side.COMMON,
        avoid(TargetedMod.BLENDTRONIC).and(m -> OptimizationsandTweaksConfig.enableMixinChunk), "core.MixinChunk"),
    common_akatsuki_MixinEntitySasori(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntitySasosri,
        "akatsuki.MixinEntitySasori"),
    common_akatsuki_MixinEntitySasori2(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntitySasosri2,
        "akatsuki.MixinEntitySasori2"),
    common_akatsuki_MixinPuppetKadz(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinPuppetKadz,
        "akatsuki.MixinPuppetKadz"),
    common_akatsuki_MixinAnimTickHandler(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinAnimTickHandler,
        "akatsuki.MixinAnimTickHandler"),
    common_akatsuki_MixinAnimationHandler(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinAnimationHandler,
        "akatsuki.MixinAnimationHandler"),
    common_aether_MixinPlayerAether(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinPlayerAether,
        "aether.MixinPlayerAether"),
    common_aether_MixinAetherTileEntities(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinAetherTileEntities,
        "aether.MixinAetherTileEntities"),
    client_essenceofthegod_MixinBarTickHandler(Side.CLIENT,
        m -> OptimizationsandTweaksConfig.enableMixindisablingguifromEssenceofthegod,
        "essenceofthegod.MixinBarTickHandler"),
    client_essenceofthegod_MixinPlayerStats(Side.CLIENT,
        m -> OptimizationsandTweaksConfig.enableMixindisablingguifromEssenceofthegod,
        "essenceofthegod.MixinPlayerStats"),
    common_etfuturumrequiem_MixinUtils(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinUtils,
        "etfuturumrequiem.MixinUtils"),
    common_notenoughpets_MixinEventHandlerNEP(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEventHandlerNEP,
        "notenoughpets.MixinEventHandlerNEP"),
    common_pneumaticraft_MixinHackTickHandler(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinHackTickHandler,
        "pneumaticraft.MixinHackTickHandler"),
    common_ic2_MixinPriorityExecutor(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinPriorityExecutor,
        "ic2.MixinPriorityExecutor"),

    common_growthcraft_MixinAppleFuelHandler(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinAppleFuelHandler,
        "growthcraft.MixinAppleFuelHandler"),
    common_core_MixinBlockGrass(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinBlockGrass,
        "core.MixinBlockGrass"),
    common_adventurersamulet_MixinEntityEagle(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityEagle,
        "adventurersamulet.MixinEntityEagle"),
    common_core_MixinEntityLiving(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityLiving,
        "core.MixinEntityLiving"),
    common_core_MixinEntityAgeable(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityAgeable,
        "core.MixinEntityAgeable"),
    common_nei_MixinNEIServerUtils(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinNEIServerUtils,
        "nei.MixinNEIServerUtils"),
    common_nei_MixinConfig(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinConfig, "ic2.MixinConfig"),
    common_core_MixinBlockLiquid(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinBlockLiquid,
        "core.MixinBlockLiquid"),
    common_core_entity_MixinEntityZombie(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityZombie,
        "core.entity.MixinEntityZombie"),

    common_core_entity_MixinEntityItem(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityItem,
        "core.entity.MixinEntityItem"),
    common_ic2_MixinTickHandler(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinTickHandler,
        "ic2.MixinTickHandler"),

    common_core_entity_MixinEntityAnimal(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityAnimal,
        "core.entity.MixinEntityAnimal"),
    common_core_entity_MixinEntitySquid(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntitySquid,
        "core.entity.MixinEntitySquid"),
    common_core_MixinEntityAITasks(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityAITasks,
        "core.MixinEntityAITasks"),
    common_core_MixinEntityMoveHelper(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityMoveHelper,
        "core.MixinEntityMoveHelper"),
    common_core_MixinWorldGenMinable(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinWorldGenMinable,
        "core.MixinWorldGenMinable"),
    common_core_MixinLeaves(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinLeaves, "core.MixinLeaves"),

    common_core_MixinEntityAIFollowParent(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinEntityAIFollowParent, "core.MixinEntityAIFollowParent"),

    common_jewelrycraft2_MixinEntityEventHandler(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinEntityEventHandler, "jewelrycraft2.MixinEntityEventHandler"),
    common_portalgun_MixinSettings(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinSettings,
        "portalgun.MixinSettings"),
    common_core_MixinEntityLookHelper(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityLookHelper,
        "core.MixinEntityLookHelper"),
    common_core_MixinBlockFalling(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinBlockFalling,
        "core.MixinBlockFalling"),

    common_core_MixinAnvilChunkLoader(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinAnvilChunkLoader,
        "core.MixinAnvilChunkLoader"),
    common_koto_MixinPatchWorldGenCloudNine(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinPatchWorldGenCloudNine, "koto.MixinPatchWorldGenCloudNine"),
    common_koto_MixinEntityDarkMiresi(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEntityDarkMiresi,
        "koto.MixinEntityDarkMiresi"),
    common_thaumcraft_MixinMappingThread(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinMappingThread,
        "thaumcraft.MixinMappingThread"),
    common_codechickencore_MixinClassDiscoverer(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinClassDiscoverer, "codechickencore.MixinClassDiscoverer"),

    common_lootpluplus_MixinLootPPHelper(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinLootPPHelper,
        "lootpluplus.MixinLootPPHelper"),
    common_matmos_MixinForgeBase(Side.COMMON, m -> OptimizationsandTweaksConfig.enableOptimizeMatmos,
        "matmos.MixinForgeBase"),
    common_matmos_MixinBlockCountModule(Side.COMMON, m -> OptimizationsandTweaksConfig.enableOptimizeMatmos,
        "matmos.MixinBlockCountModule"),

    common_matmos_MixinScanVolumetric(Side.COMMON, m -> OptimizationsandTweaksConfig.enableOptimizeMatmos,
        "matmos.MixinScanVolumetric"),

    common_matmos_MixinScanRaycast(Side.COMMON, m -> OptimizationsandTweaksConfig.enableOptimizeMatmos,
        "matmos.MixinScanRaycast"),
    common_matmos_MixinScannerModule(Side.COMMON, m -> OptimizationsandTweaksConfig.enableOptimizeMatmos,
        "matmos.MixinScannerModule"),
    common_matmos_MixinSheetDataPackage(Side.COMMON, m -> OptimizationsandTweaksConfig.enableOptimizeMatmos,
        "matmos.MixinSheetDataPackage"),
    common_thaumcraftminusthaumcraft_MixinUnthaumic(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinUnthaumic,
        "thaumcraftminusthaumcraft.MixinUnthaumic"),
    common_thaumcraft_MixinPatchBiomeGenMagicalForest(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinPatchBiomeGenMagicalForest,
        "thaumcraft.MixinPatchBiomeGenMagicalForest"),
    common_thaumcraft_MixinPatchBlockMagicalLeavesPerformances(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinPatchBlockMagicalLeavesPerformances,
        "thaumcraft.MixinPatchBlockMagicalLeavesPerformances"),

    // BUGFIX MIXINS
    common_ragdollcorpse_MixinEventHandler_Ragdoll(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinRagdollCorpse,
        "ragdollcorpse.MixinEventHandler_Ragdoll"),
    common_thaumcraft_MixinScanManager(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinScanManager,
        "thaumcraft.MixinScanManager"),
    common_eternalfrost_MixinEFConfiguration(Side.COMMON,
        require(TargetedMod.ENDLESSIDS).and(m -> OptimizationsandTweaksConfig.enableMixinEFConfiguration),
        "eternalfrost.MixinEFConfiguration"),

    common_blocklings_MixinItemBlockling(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinItemBlockling,
        "blocklings.MixinItemBlockling"),
    common_nei_MixinWorldOverlayRenderer(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinWorldOverlayRenderer,
        "nei.MixinWorldOverlayRenderer"),
    common_buildcraft_addon_oiltweaks_MixinBuildCraftConfig(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinBuildCraftConfig,
        "buildcraft.addon.oiltweaks.MixinBuildCraftConfig"),

    common_diseasecraft_MixinMedUtils(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinMedUtils,
        "diseasecraft.MixinMedUtils"),

    common_industrialupgrade_MixinRegisterOreDict(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinRegisterOreDict, "industrialupgrade.MixinRegisterOreDict"),
    common_gemsnjewels_MixinModBlocksGemsNJewels(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinModBlocksGemsNJewels, "gemsnjewels.MixinModBlocksGemsNJewels"),
    common_farlanders_MixinEntityEnderGolem(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityEnderGolem"),
    common_farlanders_MixinEntityEnderminion(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityEnderminion"),
    common_farlanders_MixinEntityFarlander(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityFarlander"),
    common_farlanders_MixinEntityLootr(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityLootr"),
    common_farlanders_MixinEntityMysticEnderminion(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityMysticEnderminion"),
    common_farlanders_MixinEntityWanderer(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityWanderer"),
    common_farlanders_MixinEntityTitan(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityTitan"),
    common_farlanders_MixinEntityRebel(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityRebel"),
    common_farlanders_MixinEntityMystic(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityMystic"),
    common_farlanders_MixinEntityEnderGuardian(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityEnderGuardian"),
    common_farlanders_MixinEntityElder(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityElder"),
    common_farlanders_MixinEntityFanEnderman(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixUnableToPlayUnknowSoundEventFromFarlandersmod,
        "farlanders.MixinEntityFanEnderman"),
    common_thaumcraft_MixinFixCascadingWorldGenFromThaumcraftWorldGenerator(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixCascadingWorldGenFromThaumcraftWorldGenerator,
        "thaumcraft.MixinFixCascadingWorldGenFromThaumcraftWorldGenerator"),
    common_thaumcraft_MixinWorldGenGreatwoodTrees(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinWorldGenGreatwoodTrees,
        "thaumcraft.MixinWorldGenGreatwoodTrees"),

    common_thaumcraft_MixinWorldGenSilverwoodTrees(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinWorldGenSilverwoodTrees,
        "thaumcraft.MixinWorldGenSilverwoodTrees"),

    common_thaumcraft_MixinWorldGenEldritchRing(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinWorldGenEldritchRing,
        "thaumcraft.MixinWorldGenEldritchRing"),

    common_thaumcraft_MixinWorldGenCustomFlowersSide(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinWorldGenCustomFlowers,
        "thaumcraft.MixinWorldGenCustomFlowers"),
    common_gardenstuff_MixinWorldGenCandelilla(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinWorldGenCandelilla,
        "gardenstuff.MixinWorldGenCandelilla"),
    common_pamsharvestcraft_MixinFixWorldGenPamFruitTree(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixWorldGenPamFruitTree,
        "pamsharvestcraft.MixinFixWorldGenPamFruitTree"),
    common_steamcraft2_MixinFixCascadingFromWorldGenBrassTree(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixCascadingFromWorldGenBrassTree,
        "steamcraft2.MixinFixCascadingFromWorldGenBrassTree"),
    common_cofhcore_fixoredictcrash_MixinOreDictionaryArbiter(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinOreDictCofhFix,
        "cofhcore.fixoredictcrash.MixinOreDictionaryArbiter"),
    common_cofhcore_MixinBlockTickingWater(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinBlockTickingWater,
        "cofhcore.MixinBlockTickingWater"),
    common_slimecarnage_MixinFixCascadingFromWorldGenSlimeCarnage(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixCascadingFromWorldGenSlimeCarnage,
        "slimecarnage.MixinFixCascadingFromWorldGenSlimeCarnage"),

    common_slimecarnage_MixinWorldGenSewers(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixCascadingFromWorldGenSlimeCarnage,
        "slimecarnage.MixinWorldGenSewers"),
    common_familliarsAPI_MixinFamiliar(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinFamiliar,
        "familliarsAPI.MixinFamiliar"),
    common_pamsharvestcraft_MixinFixPamsTreesCascadingWorldgenLag(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixPamsTreesCascadingWorldgenLag,
        "pamsharvestcraft.MixinFixPamsTreesCascadingWorldgenLag"),
    common_hardcorewither_MixinEventHandler(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinEventHandler,
        "hardcorewither.MixinEventHandler"),
    common_shincolle_MixinEVENT_BUS_EventHandler(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinEVENT_BUS_EventHandler, "shincolle.MixinEVENT_BUS_EventHandler"),
    common_betterburning_MixinBetterBurning(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinBetterBurning,
        "betterburning.MixinBetterBurning"),
    common_MixinFixCascadingFromShipwreckGen(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixCascadingFromShipwreckGen,
        "shipwreck.MixinFixCascadingFromShipwreckGen"),
    common_shincolle_MixinFixCascadingFromWorldGenPolyGravel(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixCascadingFromWorldGenPolyGravel,
        "shincolle.MixinFixCascadingFromWorldGenPolyGravel"),
    common_shincolle_MixinFixCascadingFromShinColleWorldGen(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinFixCascadingFromShinColleWorldGen,
        "shincolle.MixinFixCascadingFromShinColleWorldGen"),

    common_minefactoryreloaded_MixinFixCascadingforMineFactoryReloadedWorldGen(Side.COMMON,
        require(TargetedMod.MINEFACTORYRELOADED)
            .and(m -> OptimizationsandTweaksConfig.enableMixinFixCascadingforMineFactoryReloadedWorldGen),
        "minefactoryreloaded.MixinFixCascadingforMineFactoryReloadedWorldGen"),
    common_minefactoryreloaded_MixinFixWorldGenLakesMetaCascadingWorldgenLag(Side.COMMON,
        require(TargetedMod.MINEFACTORYRELOADED).and(
            m -> OptimizationsandTweaksConfig.enableMixinFixWorldGenLakesMetaMinefactoryReloadedCascadingWorldgenFix),
        "minefactoryreloaded.MixinFixWorldGenLakesMetaCascadingWorldgenLag"),
    common_minefactoryreloaded_MixinFixRubberTreesCascadingWorldgenLag(Side.COMMON,
        require(TargetedMod.MINEFACTORYRELOADED)
            .and(m -> OptimizationsandTweaksConfig.enableMixinFixRubberTreesMinefactoryReloadedCascadingWorldgenFix),
        "minefactoryreloaded.MixinFixRubberTreesCascadingWorldgenLag"),
    common_minefactoryreloaded_MixinFixNoSuchMethodException(Side.COMMON,
        require(TargetedMod.MINEFACTORYRELOADED)
            .and(m -> OptimizationsandTweaksConfig.enableMixinFixNoSuchMethodException),
        "minefactoryreloaded.MixinFixNoSuchMethodException"),

    common_core_MixinGodZillaFix(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinGodZillaFix,
        "core.MixinGodZillaFix"),
    common_opis_MixinopisProfilerEvent(Side.COMMON, require(TargetedMod.OPIS).and(m -> OptimizationsandTweaksConfig.enableMixinopisProfilerEvent),
        "opis.MixinopisProfilerEvent"),
    common_core_MixinStatList(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinStatList, "core.MixinStatList"),

    common_malcore_MixinVersionInfo(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinVersionInfo,
        "malcore.MixinVersionInfo"),

    common_kitchencraft_MixinKitchenCraftMachines(Side.COMMON,
        m -> OptimizationsandTweaksConfig.enableMixinKitchenCraftMachines, "kitchencraft.MixinKitchenCraftMachines"),

    // CLIENT MIXINS

    client_core_MixinTextureManager(Side.CLIENT, m -> OptimizationsandTweaksConfig.enableMixinTextureManager,
        "core.MixinTextureManager"),
    client_core_MixinEntitySpellParticleFX(Side.CLIENT,
        m -> OptimizationsandTweaksConfig.enableMixinEntitySpellParticleFX, "core.MixinEntitySpellParticleFX"),
    client_core_MixinCodecIBXM(Side.CLIENT, m -> OptimizationsandTweaksConfig.enableMixinCodecIBXM,
        "core.MixinCodecIBXM"),

    client_core_MixinTesselator(Side.CLIENT,
        avoid(TargetedMod.OPTIFINE).and(m -> OptimizationsandTweaksConfig.enableMixinTesselator),
        "core.MixinTesselator"),
    /*
     * common_core_MixinPatchSpawnerAnimals(Side.COMMON,
     * avoid(TargetedMod.JAS)
     * .and(avoid(TargetedMod.DRAGONBLOCKC))
     * .and(m -> OptimizationsandTweaksConfig.enableMixinPatchSpawnerAnimals),
     * "core.MixinPatchSpawnerAnimals"),
     */

    common_core_MixinPatchSpawnerAnimals(Side.COMMON, m -> OptimizationsandTweaksConfig.enableMixinPatchSpawnerAnimals,
        "core.MixinPatchSpawnerAnimals"),
    client_core_MixinOpenGlHelper(Side.CLIENT, m -> OptimizationsandTweaksConfig.enableMixinOpenGlHelper,
        "core.MixinOpenGlHelper"),
    client_core_MixinEntityRenderer(Side.CLIENT, avoid(TargetedMod.OPTIFINE).and(avoid(TargetedMod.FASTCRAFT))
        .and(m -> OptimizationsandTweaksConfig.enableMixinEntityRenderer), "core.MixinEntityRenderer"),
    client_core_MixinModelRenderer(Side.CLIENT,
        avoid(TargetedMod.OPTIFINE).and(m -> OptimizationsandTweaksConfig.enableMixinModelRenderer),
        "core.MixinModelRenderer"),
    client_core_MixinTextureUtil(Side.CLIENT, m -> OptimizationsandTweaksConfig.enableMixinTextureUtil,
        "core.MixinTextureUtil"),
    client_core_MixinItemRenderer(Side.CLIENT, m -> OptimizationsandTweaksConfig.enableMixinItemRenderer,
        "core.MixinItemRenderer"),
    client_core_MixinFontRenderer(
        Side.CLIENT,
        avoid(TargetedMod.OPTIFINE).and(m -> OptimizationsandTweaksConfig.enableMixinFontRenderer).and(avoid(TargetedMod.SMOOTHFONT)),
        "core.MixinFontRenderer"
    ),

    // MOD-FILTERED MIXINS

    // The modFilter argument is a predicate, so you can also use the .and(), .or(), and .negate() methods to mix and
    // match multiple predicates.
    ;

    @Getter
    public final Side side;
    @Getter
    public final Predicate<List<ITargetedMod>> filter;
    @Getter
    public final String mixin;

    static Predicate<List<ITargetedMod>> require(TargetedMod in) {
        return modList -> modList.contains(in);
    }

    static Predicate<List<ITargetedMod>> avoid(TargetedMod in) {
        return modList -> !modList.contains(in);
    }
}
