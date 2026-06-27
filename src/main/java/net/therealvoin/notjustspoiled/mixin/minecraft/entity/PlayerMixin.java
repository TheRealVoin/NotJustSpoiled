package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @ModifyArg(method = "setItemSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private Object changeFoodEnvironmentWhenPlacedInEquipmentSlot(Object itemStack) {
        FoodSpoilageManager.changeEnvironmentAndUpdate((ItemStack) itemStack, FoodEnvironment.INVENTORY, ((Entity)(Object)this).level());
        return itemStack;
    }
}