package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;

@Mixin(AbstractStoveBlock.class)
public class AbstractStoveBlockMixin {
    @Inject(method = "ignite", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    private void updateFoodWhenStoveIgnited(Entity entity, LevelAccessor level, BlockPos pos, BlockState state, CallbackInfo ci) {
        notJustSpoiled$updateFood((Level) level, pos, FoodEnvironment.COOKING);
    }

    @Inject(method = "extinguish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    private void updateFoodWhenStoveExtinguished(Entity entity, LevelAccessor level, BlockPos pos, BlockState state, CallbackInfo ci) {
        notJustSpoiled$updateFood((Level) level, pos, FoodEnvironment.GROUND);
    }

    @Unique
    private static void notJustSpoiled$updateFood(Level level, BlockPos pos, FoodEnvironment foodEnvironment) {
        ItemStackHandler inventory = ((AbstractStoveBlockEntity) level.getBlockEntity(pos)).getItems();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, foodEnvironment, level);
        }
    }
}