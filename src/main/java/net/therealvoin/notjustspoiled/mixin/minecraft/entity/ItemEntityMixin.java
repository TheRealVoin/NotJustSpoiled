package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "tryToMerge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;areMergable(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", shift = At.Shift.BEFORE))
    private void updateFood(ItemEntity itemEntity, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemStack1, @Local(ordinal = 1) ItemStack itemStack2) {
        FoodSpoilageManager.tryAverageSpoilageOnMerge(itemStack1, itemStack2, itemEntity.level());
    }
}