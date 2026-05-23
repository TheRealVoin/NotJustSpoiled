package net.therealvoin.notjustspoiled.mixin.minecraft.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Inject(method = "burn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void copySpoilageCapToCookedFood(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) NonNullList<ItemStack> inventory) {
        NJSUtils.copyCapability(inventory.get(0), inventory.get(2), ((BlockEntity)(Object)this).getLevel());
    }

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    private static void changeFoodEnvironmentWhenFurnaceBecomesLitOrUnlit(Level level, BlockPos blockPos, BlockState blockState, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        FoodEnvironment foodEnvironment = blockState.getValue(AbstractFurnaceBlock.LIT) ? FoodEnvironment.COOKING : FoodEnvironment.STORAGE;
        FoodSpoilageManager.changeEnvironmentAndUpdate(blockEntity.getItem(0), foodEnvironment, level);
    }
}