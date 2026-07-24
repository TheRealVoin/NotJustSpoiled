package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @WrapOperation(method = "areMergable", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;equals(Ljava/lang/Object;)Z"))
    private static boolean removeSpoilageTagFromEqualityCheck(CompoundTag destinationTag, Object originTag, Operation<Boolean> originalMethod, ItemStack destinationStack, ItemStack originStack) {
        FoodSpoilage destinationSpoilage = FoodSpoilage.of(destinationStack);
        FoodSpoilage originSpoilage = FoodSpoilage.of(originStack);

        if (destinationSpoilage == null || originSpoilage == null) {
            return originalMethod.call(destinationTag, originTag);
        }

        Level level = NJSUtils.getLevelWithoutContext();
        FoodStatus destinationStatus = FoodSpoilageManager.getFoodStatus(destinationStack, level);
        FoodStatus originStatus = FoodSpoilageManager.getFoodStatus(originStack, level);

        if (destinationStatus != originStatus) {
            return originalMethod.call(destinationTag, originTag);
        }

        CompoundTag destinationTagWithoutSpoilage = destinationTag.copy();
        CompoundTag originTagWithoutSpoilage = ((CompoundTag) originTag).copy();
        destinationTagWithoutSpoilage.remove("food_spoilage");
        originTagWithoutSpoilage.remove("food_spoilage");

        return originalMethod.call(destinationTagWithoutSpoilage, originTagWithoutSpoilage);
    }

    @Inject(method = "merge(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;min(II)I", ordinal = 1, shift = At.Shift.AFTER))
    private static void averageFoodLifetimeBeforeMerge(ItemStack destinationStack, ItemStack originStack, int amount, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 1) int movedAmount) {
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(destinationStack, FoodEnvironment.OPEN_AIR, originStack, FoodEnvironment.OPEN_AIR, movedAmount, NJSUtils.getLevelWithoutContext());
    }
}