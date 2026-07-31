package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Shadow @Final public NonNullList<Slot> slots;
    @Shadow private ItemStack carried;

    @ModifyArgs(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void removeSpoilageTagFromEqualityCheck1(Args args, @Local Slot slot) {
        NJSUtils.removeSpoilageTagFromEqualityCheck(args, NJSUtils.getLevelBySlot(slot));
    }

    @Inject(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setCount(I)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void averageFoodLifetimeBeforeMerge1(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) ItemStack stackToPlaceInSlot, @Local(ordinal = 1) ItemStack stackInSlot, @Local Slot slot) {
        FoodEnvironment foodEnvironment = slot.container instanceof Inventory ? FoodEnvironment.INVENTORY : FoodEnvironment.STORAGE;
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInSlot, foodEnvironment, stackToPlaceInSlot, foodEnvironment, stackToPlaceInSlot.getCount(), NJSUtils.getLevelBySlot(slot));
    }

    @Inject(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V", shift = At.Shift.AFTER))
    private void averageFoodLifetimeBeforeMerge2(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) ItemStack stackToPlaceInSlot, @Local(ordinal = 1) ItemStack stackInSlot, @Local Slot slot, @Local(ordinal = 4) int maxSize) {
        FoodEnvironment foodEnvironment = slot.container instanceof Inventory ? FoodEnvironment.INVENTORY : FoodEnvironment.STORAGE;
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInSlot, foodEnvironment, stackToPlaceInSlot, foodEnvironment, maxSize - stackInSlot.getCount(), NJSUtils.getLevelBySlot(slot));
    }

    @Inject(method = "setCarried", at = @At("HEAD"))
    private void changeFoodEnvironmentWhenPlacedInCarriedSlot(ItemStack itemStack, CallbackInfo ci) {
        Level level = null;
        for (Slot slot : this.slots) {
            level = NJSUtils.getLevelBySlot(slot);
            if (level != null) {
                break;
            }
        }

        if (this.carried.isEmpty()) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, FoodEnvironment.INVENTORY, level);
        }
    }

    @ModifyArgs(method = "canItemQuickReplace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private static void removeSpoilageTagFromEqualityCheck2(Args args, @Local(argsOnly = true) Slot slot) {
        NJSUtils.removeSpoilageTagFromEqualityCheck(args, NJSUtils.getLevelBySlot(slot));
    }

    @ModifyArgs(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void removeSpoilageTagFromEqualityCheck3(Args args, @Local Slot slot) {
        NJSUtils.removeSpoilageTagFromEqualityCheck(args, NJSUtils.getLevelBySlot(slot));
    }

    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"))
    private void averageFoodLifetimeBeforeMerge3(ItemStack carriedStack, int increment, Operation<Void> originalMethod, @Local(ordinal = 2) ItemStack stackToAddToCarried) {
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(carriedStack, FoodEnvironment.INVENTORY, stackToAddToCarried, FoodEnvironment.INVENTORY, increment, NJSUtils.getLevelWithoutContext());
        originalMethod.call(carriedStack, increment);
    }
}