package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;

@Mixin(value = StoveBlock.class, remap = false)
public class StoveBlockMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    private void updateFoodWhenStoveIgnited(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        notJustSpoiled$updateFood(level, pos, FoodEnvironment.COOKING);
    }

    @Inject(method = "extinguish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    private void updateFoodWhenStoveExtinguished(BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        notJustSpoiled$updateFood(level, pos, FoodEnvironment.GROUND);
    }

    @Unique
    private static void notJustSpoiled$updateFood(Level level, BlockPos pos, FoodEnvironment foodEnvironment) {
        if (level.isClientSide()) {
            return;
        }

        ItemStackHandler inventory = ((StoveBlockEntity) level.getBlockEntity(pos)).getInventory();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, foodEnvironment, level);
        }
    }
}