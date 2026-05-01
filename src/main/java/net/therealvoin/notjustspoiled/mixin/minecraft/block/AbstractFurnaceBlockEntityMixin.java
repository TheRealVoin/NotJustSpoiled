package net.therealvoin.notjustspoiled.mixin.minecraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
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

import javax.annotation.Nullable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Inject(method = "burn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void copyCapabilityToCookedFood(RegistryAccess registryAccess, @Nullable Recipe<?> recipe, NonNullList<ItemStack> inventory, int maxStackSize, CallbackInfoReturnable<Boolean> cir) {
        Level level = ((AbstractFurnaceBlockEntity) (Object) this).getLevel();

        if (level instanceof ServerLevel serverLevel) {
            NJSUtils.copyCapability(inventory.get(0), inventory.get(2), serverLevel);
        }
    }

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    private static void changeFoodEnvironmentWhenFurnaceBecomesLitOrUnlit(Level level, BlockPos blockPos, BlockState blockState, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < blockEntity.getContainerSize(); i++) {
                ItemStack itemStack = blockEntity.getItem(i);

                FoodEnvironment foodEnvironment = blockState.getValue(AbstractFurnaceBlock.LIT) ? FoodEnvironment.COOKING : FoodEnvironment.STORAGE;
                FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, foodEnvironment, serverLevel);
            }
        }
    }
}