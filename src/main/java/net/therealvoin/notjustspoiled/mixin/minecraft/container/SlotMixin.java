package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import com.llamalad7.mixinextras.sugar.Local;
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

@Mixin(Slot.class)
public abstract class SlotMixin {
    @ModifyArgs(method = "safeInsert(Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void removeSpoilageTagFromEqualityCheck(Args args) {
        NJSUtils.removeSpoilageTagFromEqualityCheck(args, NJSUtils.getLevelBySlot((Slot)(Object)this));
    }

    @Inject(method = "safeInsert(Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V", shift = At.Shift.BEFORE))
    private void mergeFood(ItemStack stackToPlaceInSlot, int increment, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 1) ItemStack stackInSlot, @Local(ordinal = 1) int movedAmount) {
        Slot slot = ((Slot)(Object)this);
        FoodEnvironment foodEnvironment = slot.container instanceof Inventory ? FoodEnvironment.INVENTORY : FoodEnvironment.STORAGE;

        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInSlot, foodEnvironment, stackToPlaceInSlot, FoodEnvironment.INVENTORY, movedAmount, NJSUtils.getLevelBySlot(slot));
    }
}