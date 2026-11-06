package net.therealvoin.notjustspoiled.compat.mixin.farmersdelight;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;

@Mixin(AbstractStoveBlockEntity.class)
public abstract class AbstractStoveBlockEntityMixin {
    @WrapOperation(method = "placeFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack createPlacedFoodSnapshot(ItemStack stackToPlaceOnStove, int amount, Operation<ItemStack> originalMethod, @Share("placedStack") LocalRef<ItemStack> placedStackSnapshot) {
        ItemStack placedStack = originalMethod.call(stackToPlaceOnStove, amount);
        placedStackSnapshot.set(placedStack);
        return placedStack;
    }

    @Inject(method = "placeFood", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.AFTER), remap = false)
    private void changeFoodEnvironmentWhenPlacedOnStove(CallbackInfoReturnable<Boolean> cir, @Share("placedStack") LocalRef<ItemStack> placedStack) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(placedStack.get(), FoodEnvironment.OPEN_AIR, ((BlockEntity)(Object)this).getLevel());
    }
}