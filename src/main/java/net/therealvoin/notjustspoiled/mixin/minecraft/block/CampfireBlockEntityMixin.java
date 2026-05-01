package net.therealvoin.notjustspoiled.mixin.minecraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {
    @Inject(method = "cookTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Containers;dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void copyCapabilityToCookedFood(Level level, BlockPos blockPos, BlockState blockState, CampfireBlockEntity campfireBlockEntity, CallbackInfo ci, boolean flag, int i, ItemStack inputItem, int j, Container container, ItemStack resultItem) {
        if (level instanceof ServerLevel serverLevel) {
            NJSUtils.copyCapability(inputItem, resultItem, serverLevel);
        }
    }

    @Inject(method = "placeFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"))
    private void changeFoodEnvironmentWhenPlacedOnCampfire(Entity entity, ItemStack itemStack, int cookingTime, CallbackInfoReturnable<Boolean> cir) {
        if (entity != null && entity.level() instanceof ServerLevel serverLevel) {
            CampfireBlockEntity campfire = (CampfireBlockEntity) (Object) this;
            FoodEnvironment environment = campfire.getBlockState().getValue(CampfireBlock.LIT) ? FoodEnvironment.COOKING : FoodEnvironment.GROUND;
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, environment, serverLevel);
        }
    }
}