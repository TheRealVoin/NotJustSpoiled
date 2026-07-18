package net.therealvoin.notjustspoiled.mixin.minecraft.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.BlockFoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;setPlacedBy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.AFTER))
    private void onPlace(CallbackInfoReturnable<InteractionResult> cir, @Local BlockPos blockpos, @Local Level level, @Local ItemStack itemstack) {
        if (level instanceof ServerLevel serverLevel && FoodCategory.of(itemstack) != null) {
            BlockFoodSpoilage data = BlockFoodSpoilage.get(serverLevel);
            ItemStack itemStackCopy = itemstack.copy();
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStackCopy, FoodEnvironment.OPEN_AIR, serverLevel);
            data.addItemStackAt(blockpos, itemStackCopy);
        }
    }
}