package net.therealvoin.notjustspoiled.mixin.minecraft.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireChargeItem.class)
public abstract class FireChargeItemMixin {
    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", shift = At.Shift.AFTER, ordinal = 1))
    private void changedFoodEnvironmentWhenCampfireIgnited(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();

        if (level.isClientSide()) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(context.getClickedPos());
        if (blockEntity instanceof CampfireBlockEntity campfire) {
            for (ItemStack itemStack : campfire.getItems()) {
                FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, FoodEnvironment.COOKING, level);
            }
        }
    }
}