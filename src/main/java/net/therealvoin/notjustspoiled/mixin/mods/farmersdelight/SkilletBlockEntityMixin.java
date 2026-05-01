package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

import java.util.Optional;

@Mixin(SkilletBlockEntity.class)
public class SkilletBlockEntityMixin {
    @Inject(method = "cookAndOutputItems", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/crafting/CampfireCookingRecipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void copyCapabilityToCookedFood(ItemStack cookingStack, Level level, CallbackInfo ci, SimpleContainer wrapper, Optional recipe, ItemStack resultStack) {
        if (level instanceof ServerLevel serverLevel) {
            NJSUtils.copyCapability(cookingStack, resultStack, serverLevel);
        }
    }
}