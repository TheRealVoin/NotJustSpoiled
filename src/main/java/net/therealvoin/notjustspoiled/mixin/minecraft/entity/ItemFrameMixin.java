package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

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
    @Inject(method = "setItem(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V", shift = At.Shift.AFTER))
    private void changeFoodEnvironmentWhenPlacedInItemFrame(ItemStack stack, boolean updateNeighbours, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stack, FoodEnvironment.GROUND, ((ItemFrame) (Object) this).level());
    }
}