package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ThrowableItemProjectile.class)
public abstract class ThrowableItemProjectileMixin {
    @ModifyArg(method = "setItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V"), index = 1)
    private Object changeFoodEnvironmentWhenThrown(Object value) {
        ItemStack stack = (ItemStack) value;
        FoodSpoilageManager.changeEnvironmentAndUpdate(stack, FoodEnvironment.OPEN_AIR, ((Entity)(Object)this).level());
        return stack;
    }
}