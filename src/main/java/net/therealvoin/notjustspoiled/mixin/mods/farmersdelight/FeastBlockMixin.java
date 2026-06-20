package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.blockfoodspoilage.BlockFoodSpoilageProvider;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.FeastBlock;

@Mixin(value = FeastBlock.class, remap = false)
public class FeastBlockMixin {
    @Inject(method = "takeServing", at = @At(value = "INVOKE_ASSIGN", target = "Lvectorwing/farmersdelight/common/block/FeastBlock;getServingItem(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    private void copySpoilageCapToFood(CallbackInfoReturnable<InteractionResult> cir, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos blockPos, @Local(name = "serving") ItemStack serving) {
        level.getCapability(BlockFoodSpoilageProvider.BLOCK_FOOD_SPOILAGE).ifPresent(blockFoodSpoilage -> {
            NJSUtils.copyCapability(blockFoodSpoilage.getLastItemStackByPos(blockPos), serving, level);
        });
    }
}