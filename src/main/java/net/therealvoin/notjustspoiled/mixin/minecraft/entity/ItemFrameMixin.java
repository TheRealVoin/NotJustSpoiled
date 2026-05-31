package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin {
    @Inject(method = "setItem(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At("TAIL"))
    private void changeFoodEnvironmentWhenPlacedInItemFrame(CallbackInfo ci, @Local(argsOnly = true) ItemStack stackToPlaceInItemFrame) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPlaceInItemFrame, FoodEnvironment.GROUND, ((ItemFrame)(Object)this).level());
    }
}