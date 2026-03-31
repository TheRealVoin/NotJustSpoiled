package net.therealvoin.notjustspoiled.mixin.minecraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin {
    @Inject(method = "dowse", at = @At("TAIL"))
    private static void changeFoodEnvironmentWhenCampfireExtinguished(Entity entity, LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (!(levelAccessor instanceof Level level) || level.isClientSide()) {
            return;
        }

        CampfireBlockEntity campfire = (CampfireBlockEntity) level.getBlockEntity(blockPos);

        for (ItemStack itemStack : campfire.getItems()) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, FoodEnvironment.GROUND, level);
        }
    }

    @Inject(method = "onProjectileHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    private void changeFoodEnvironmentWhenCampfireIgnitedByProjectile(Level level, BlockState blockState, BlockHitResult hitResult, Projectile projectile, CallbackInfo ci) {
        if (level.isClientSide()) {
            return;
        }

        CampfireBlockEntity campfire = (CampfireBlockEntity) level.getBlockEntity(hitResult.getBlockPos());

        for (ItemStack itemStack : campfire.getItems()) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, FoodEnvironment.COOKING, level);
        }
    }
}