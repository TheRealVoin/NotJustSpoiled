package net.therealvoin.notjustspoiled.compat.mixin.survival_instinct;

import com.llamalad7.mixinextras.sugar.Local;
import net.mcreator.survivalinstinct.world.inventory.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({
        CashRegisterGUIMenu.class,
        EmptyBagGUIMenu.class,
        GabageBagGUIMenu.class,
        RefrigeratorGUIMenu.class,
        TrashCanGUIMenu.class
})
public abstract class GUIMenuMixin {
    @ModifyArgs(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void removeSpoilageTagFromEqualityCheck1(Args args, @Local(name = "slot") Slot slot) {
        NJSUtils.removeSpoilageTagFromEqualityCheck(args, NJSUtils.getLevelBySlot(slot));
    }

    @Inject(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setCount(I)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void averageFoodLifetimeBeforeMerge1(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) ItemStack stackToPlaceInSlot, @Local(name = "itemstack") ItemStack stackInSlot, @Local(name = "slot") Slot slot) {
        FoodEnvironment foodEnvironment = slot.container instanceof Inventory ? FoodEnvironment.INVENTORY : FoodEnvironment.STORAGE;
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInSlot, foodEnvironment, stackToPlaceInSlot, foodEnvironment, stackToPlaceInSlot.getCount(), NJSUtils.getLevelBySlot(slot));
    }

    @Inject(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V", shift = At.Shift.AFTER))
    private void averageFoodLifetimeBeforeMerge2(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) ItemStack stackToPlaceInSlot, @Local(name = "itemstack") ItemStack stackInSlot, @Local(name = "slot") Slot slot, @Local(name = "maxSize") int maxSize) {
        FoodEnvironment foodEnvironment = slot.container instanceof Inventory ? FoodEnvironment.INVENTORY : FoodEnvironment.STORAGE;
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInSlot, foodEnvironment, stackToPlaceInSlot, foodEnvironment, maxSize - stackInSlot.getCount(), NJSUtils.getLevelBySlot(slot));
    }
}