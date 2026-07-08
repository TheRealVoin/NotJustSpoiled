package net.therealvoin.notjustspoiled.compat.mixin.farmersdelight;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.entity.BasketBlockEntity;

@Mixin(BasketBlockEntity.class)
public abstract class BasketBlockEntityMixin {
    @Inject(method = "setItem", at = @At("TAIL"))
    private void updateFoodWhenPlacedInBasket(int slot, ItemStack stack, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stack, FoodEnvironment.STORAGE, ((BlockEntity)(Object)this).getLevel());
    }
}