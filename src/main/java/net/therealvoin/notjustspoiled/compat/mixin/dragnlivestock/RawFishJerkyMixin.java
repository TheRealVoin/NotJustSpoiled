package net.therealvoin.notjustspoiled.compat.mixin.dragnlivestock;

import com.dragn0007.dragnlivestock.blocks.custom.RawFishJerky;
import com.dragn0007.dragnlivestock.items.LOItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.therealvoin.notjustspoiled.common.foodspoilage.BlockFoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RawFishJerky.class)
public abstract class RawFishJerkyMixin {
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 1))
    private void copySpoilageToResultStack(BlockState state, ServerLevel level, BlockPos pos, RandomSource source, CallbackInfo ci) {
        BlockFoodSpoilage data = BlockFoodSpoilage.get(level);
        ItemStack stack = new ItemStack(LOItems.FISH_JERKY.get());
        FoodSpoilageManager.copySpoilage(data.getLastItemStackAt(pos), stack, level);
        data.removeItemStackAt(pos, data.getLastItemStackAt(pos));
        data.addItemStackAt(pos, stack);
    }
}