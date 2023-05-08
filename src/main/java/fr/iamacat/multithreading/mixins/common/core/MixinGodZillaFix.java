package fr.iamacat.multithreading.mixins.common.core;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeInternalHandler;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import cpw.mods.fml.common.FMLLog;
import fr.iamacat.multithreading.config.MultithreadingandtweaksMultithreadingConfig;

@Mixin(ForgeInternalHandler.class)
public class MixinGodZillaFix {

    @Redirect(
        method = "onEntityJoinWorld",
        at = @At(
            value = "INVOKE",
            target = "Lcpw/mods/fml/common/FMLLog;warning(Ljava/lang/String;[Ljava/lang/Object;)V"),
        remap = false)
    private void onEntityJoinWorldRedirect(String message, Object[] params, EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityItem && MultithreadingandtweaksMultithreadingConfig.enableMixinGodZillaFix) {
            EntityItem entityItem = (EntityItem) event.entity;
            ItemStack itemStack = entityItem.getEntityItem();

            if (itemStack == null || itemStack.getItem() == null) {
                return;
            }
        } else {
            FMLLog.warning(message, params);
        }
    }
}
