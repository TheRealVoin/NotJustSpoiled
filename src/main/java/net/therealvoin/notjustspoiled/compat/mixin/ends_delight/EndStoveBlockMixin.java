package net.therealvoin.notjustspoiled.compat.mixin.ends_delight;

import cn.foggyhillside.ends_delight.block.EndStoveBlock;
import cn.foggyhillside.ends_delight.blockentitiy.EndStoveBlockEntity;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndStoveBlock.class)
public abstract class EndStoveBlockMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    private void updateFoodWhenStoveIgnited(CallbackInfoReturnable<InteractionResult> cir, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos) {
        notJustSpoiled$updateFood(level, pos, FoodEnvironment.COOKING);
    }

    @Inject(method = "extinguish", at = @At("TAIL"), remap = false)
    private void updateFoodWhenStoveExtinguished(BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        notJustSpoiled$updateFood(level, pos, FoodEnvironment.OPEN_AIR);
    }

    @Unique
    private static void notJustSpoiled$updateFood(Level level, BlockPos pos, FoodEnvironment foodEnvironment) {
        ItemStackHandler inventory = ((EndStoveBlockEntity) level.getBlockEntity(pos)).getInventory();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, foodEnvironment, level);
        }
    }
}