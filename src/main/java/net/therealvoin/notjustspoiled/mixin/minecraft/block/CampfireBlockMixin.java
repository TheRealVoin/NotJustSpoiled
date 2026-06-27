package net.therealvoin.notjustspoiled.mixin.minecraft.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin {
    @Inject(method = "dowse", at = @At("TAIL"))
    private static void changeFoodEnvironmentWhenCampfireExtinguished(CallbackInfo ci, @Local(argsOnly = true) LevelAccessor levelAccessor, @Local(argsOnly = true) BlockPos blockPos) {
        if (levelAccessor instanceof Level level) {
            CampfireBlockEntity campfire = (CampfireBlockEntity) level.getBlockEntity(blockPos);

            for (ItemStack foodStack : campfire.getItems()) {
                FoodSpoilageManager.changeEnvironmentAndUpdate(foodStack, FoodEnvironment.OPEN_AIR, level);
            }
        }
    }

    @Inject(method = "onProjectileHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    private void changeFoodEnvironmentWhenCampfireIgnitedByProjectile(CallbackInfo ci, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockHitResult hitResult) {
        CampfireBlockEntity campfire = (CampfireBlockEntity) level.getBlockEntity(hitResult.getBlockPos());

        for (ItemStack foodStack : campfire.getItems()) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(foodStack, FoodEnvironment.COOKING, level);
        }
    }
}