package net.therealvoin.notjustspoiled.mixin.forge;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = ItemStackHandler.class, remap = false)
public abstract class ItemStackHandlerMixin {
    @ModifyArgs(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemHandlerHelper;canItemStacksStack(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void removeSpoilageTagFromEqualityCheck(Args args) {
        NJSUtils.removeSpoilageTagFromEqualityCheck(args, NJSUtils.getLevelWithoutContext());
    }

    @WrapOperation(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"))
    private void averageFoodLifetimeBeforeMerge(ItemStack stackInSLot, int increment, Operation<Void> originalMethod, @Local(argsOnly = true) ItemStack stackToSetInSlot) {
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInSLot, FoodEnvironment.STORAGE, stackToSetInSlot, FoodEnvironment.STORAGE, increment, NJSUtils.getLevelWithoutContext());
    }

    @ModifyArg(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private Object changeFoodEnvironmentWhenPlacedInContainer(Object value) {
        ItemStack stackToSetInSlot = (ItemStack) value;
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToSetInSlot, FoodEnvironment.STORAGE, NJSUtils.getLevelWithoutContext());
        return stackToSetInSlot;
    }

    @ModifyArg(method = "setStackInSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private Object changeFoodEnvironmentWhenPlacedInContainer2(Object value) {
        ItemStack stackToSetInSlot = (ItemStack) value;
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToSetInSlot, FoodEnvironment.STORAGE, NJSUtils.getLevelWithoutContext());
        return stackToSetInSlot;
    }
}