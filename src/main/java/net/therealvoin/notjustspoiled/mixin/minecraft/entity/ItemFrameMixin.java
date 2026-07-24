package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin {
    @ModifyArg(method = "setItem(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V"), index = 1)
    private Object changeFoodEnvironmentWhenPlacedInItemFrame(Object value) {
        ItemStack stackToPlaceInItemFrame = (ItemStack) value;
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPlaceInItemFrame, FoodEnvironment.OPEN_AIR, ((Entity)(Object)this).level());
        return stackToPlaceInItemFrame;
    }
}