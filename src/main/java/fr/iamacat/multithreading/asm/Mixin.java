package fr.iamacat.multithreading.asm;

import java.util.*;
import java.util.function.Predicate;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;

import fr.iamacat.multithreading.config.MultithreadingandtweaksMultithreadingConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Mixin implements IMixin {

    // MULTITHREADING MIXINS
    common_core_MixinLeafDecay(Side.COMMON, m -> MultithreadingandtweaksMultithreadingConfig.enableMixinLeafDecay,
        "core.MixinLeafDecay"),
    common_core_MixinEntityAITask(Side.COMMON, m -> MultithreadingandtweaksMultithreadingConfig.enableMixinEntityAITask,
        "core.MixinEntityAITask"),
    common_core_MixinMixinEntityUpdate(Side.COMMON,
        m -> MultithreadingandtweaksMultithreadingConfig.enableMixinEntityUpdate, "core.MixinEntityUpdate"),
    common_core_MixinFireTick(Side.COMMON, m -> MultithreadingandtweaksMultithreadingConfig.enableMixinFireTick,
        "core.MixinFireTick"),
    common_core_MixinGrowthSpreading(Side.COMMON,
        m -> MultithreadingandtweaksMultithreadingConfig.enableMixinGrowthSpreading, "core.MixinGrowthSpreading"),
    common_core_MixinLiquidTick(Side.COMMON, m -> MultithreadingandtweaksMultithreadingConfig.enableMixinliquidTick,
        "core.MixinLiquidTick"),
    common_core_MixinEntitySpawning(Side.COMMON,
        m -> MultithreadingandtweaksMultithreadingConfig.enableMixinEntitySpawning, "core.MixinEntitySpawning"),
    common_core_MixinChunkPopulating(Side.COMMON,
        m -> MultithreadingandtweaksMultithreadingConfig.enableMixinChunkPopulating, "core.MixinChunkPopulating"),

    common_core_MixinEntityLightningBolt(Side.COMMON,
        m -> MultithreadingandtweaksMultithreadingConfig.enableMixinEntityLightningBolt,
        "core.MixinEntityLightningBolt"),
    common_core_MixinParticle(Side.COMMON, m -> MultithreadingandtweaksMultithreadingConfig.enableMixinParticle,
        "core.MixinParticleManager"),
    common_core_MixinEntitiesTick(Side.COMMON, m -> MultithreadingandtweaksMultithreadingConfig.enableMixinEntitiesTick,
        "core.MixinEntitiesTick"),
    common_core_MixinTileEntitiesTick(Side.COMMON,
        m -> MultithreadingandtweaksMultithreadingConfig.enableMixinTileEntitiesTick, "core.MixinTileEntitiesTick"),
    common_core_MixinWorldgen(Side.COMMON, m -> MultithreadingandtweaksMultithreadingConfig.enableMixinWorldgen,
        "core.MixinWorldgen"),

    common_core_MixinWorldTick(Side.COMMON, m -> MultithreadingandtweaksMultithreadingConfig.enableMixinWorldTick,
        "core.MixinWorldTick"),

    // CLIENT MIXINS
    client_core_MixinEntitiesRendering(Side.CLIENT,
        m -> MultithreadingandtweaksMultithreadingConfig.enableMixinEntitiesRendering, "core.MixinEntitiesRendering"),
    client_core_MixinTileEntities(Side.CLIENT, m -> MultithreadingandtweaksMultithreadingConfig.enableMixinTileEntities,
        "core.MixinTileEntities"),
    client_core_MixinGUIHUD(Side.CLIENT, m -> MultithreadingandtweaksMultithreadingConfig.enableMixinGUIHUD,
        "core.MixinGUIHUD"),
    client_core_MixinRemoveLightning(Side.CLIENT,
        m -> MultithreadingandtweaksMultithreadingConfig.disablelightningupdate, "core.MixinRemoveLightning"),

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
}