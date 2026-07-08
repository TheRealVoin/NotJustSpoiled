package net.therealvoin.notjustspoiled.compat.mixin.eanimod;

import mokiyoki.enhancedanimals.tileentity.ChickenNestTileEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChickenNestTileEntity.class)
public abstract class ChickenNestTileEntityMixin {
    @Inject(method = "addEggToNest", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER), remap = false)
    private void updateFoodWhenPlacedInChickenNest(Level level, ItemStack stackToPutInNest, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPutInNest, FoodEnvironment.OPEN_AIR, level);
    }

    @Inject(method = "removeItem", at = @At("RETURN"))
    private void updateFoodWhenPlacedInInventory(CallbackInfoReturnable<ItemStack> cir) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(cir.getReturnValue(), FoodEnvironment.INVENTORY, ((BlockEntity)(Object)this).getLevel());
    }
}