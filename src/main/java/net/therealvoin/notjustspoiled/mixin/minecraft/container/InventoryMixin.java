package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow @Final public Player player;

    @ModifyArg(method = "setItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private Object changeFoodEnvironmentWhenPlacedInInventory(Object stackToPlaceInInventory) {
        FoodSpoilageManager.changeEnvironmentAndUpdate((ItemStack) stackToPlaceInInventory, FoodEnvironment.INVENTORY, player.level());
        return stackToPlaceInInventory;
    }

    @ModifyArgs(method = "hasRemainingSpaceForItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void removeSpoilageTagFromEqualityCheck(Args args) {
        NJSUtils.removeSpoilageTagFromEqualityCheck(args, player.level());
    }

    @WrapOperation(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"))
    private void averageFoodLifetimeBeforeMerge(ItemStack stackInSlot, int increment, Operation<Void> originalMethod, @Local(argsOnly = true) ItemStack stackToPlaceInSlot) {
        Level level = player.level();
        if (stackInSlot.isEmpty()) {
            originalMethod.call(stackInSlot, increment);
            FoodSpoilageManager.changeEnvironmentAndUpdate(stackInSlot, FoodEnvironment.INVENTORY, level);
        } else {
            FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInSlot, FoodEnvironment.INVENTORY, stackToPlaceInSlot, FoodEnvironment.OPEN_AIR, increment, level);
            originalMethod.call(stackInSlot, increment);
        }
    }
}