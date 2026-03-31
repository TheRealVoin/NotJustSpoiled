package net.therealvoin.notjustspoiled.mixin.minecraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.core.foodspoilage.capability.blockfoodspoilage.BlockFoodSpoilageProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;setPlacedBy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onPlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir, BlockPlaceContext blockplacecontext, BlockState blockstate, BlockPos blockpos, Level level, Player player, ItemStack itemstack, BlockState blockstate1) {
        if (level.isClientSide() || FoodCategory.getFoodCategory(itemstack) == null) {
            return;
        }

        level.getCapability(BlockFoodSpoilageProvider.BLOCK_FOOD_SPOILAGE).ifPresent(blockFoodSpoilage -> {
            ItemStack itemStackCopy = itemstack.copy();
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStackCopy, FoodEnvironment.GROUND, level);
            blockFoodSpoilage.putItemStackPos(blockpos, itemStackCopy);
        });
    }
}