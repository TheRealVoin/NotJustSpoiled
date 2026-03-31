package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.core.foodspoilage.capability.foodspoilage.FoodSpoilageProvider;
import net.therealvoin.notjustspoiled.util.NJSUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Shadow @Final public NonNullList<Slot> slots;
    @Shadow private ItemStack carried;

    @Inject(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void makeFoodPossibleForMerging(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection, CallbackInfoReturnable<Boolean> cir, boolean flag, int i, Slot slot, ItemStack itemstack) {
        Level level = NJSUtils.getLevelBySlot(slot);

        if (level == null || level.isClientSide()) {
            return;
        }

        itemstack.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
            FoodSpoilageManager.changeEnvironmentAndUpdate(stack, foodSpoilage.getEnvironment(), level);
            FoodSpoilageManager.tryAverageSpoilageOnMerge(stack, itemstack, level);
        });
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

        if (level == null || level.isClientSide()) {
            return;
        }

        if (this.carried.isEmpty() && !itemStack.isEmpty()) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, FoodEnvironment.INVENTORY, level);
        }
    }

    @Inject(method = "canItemQuickReplace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private static void makeFoodPossibleForMerging(Slot slot, ItemStack stack, boolean stackSizeMatters, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemInSlot = slot.getItem();
        Level level = NJSUtils.getLevelBySlot(slot);

        if (level == null || level.isClientSide()) {
            return;
        }

        itemInSlot.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
            FoodSpoilageManager.changeEnvironmentAndUpdate(stack, foodSpoilage.getEnvironment(), level);
            FoodSpoilageManager.tryAverageSpoilageOnMerge(itemInSlot, stack, level);
        });
    }
}