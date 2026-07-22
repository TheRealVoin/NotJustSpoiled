package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    private static boolean modifyEqualityCheck(CompoundTag instance, Object pOther, Operation<Boolean> original, ItemStack itemStack1, ItemStack itemStack2) {
        FoodSpoilage foodSpoilage1 = FoodSpoilage.of(itemStack1);
        FoodSpoilage foodSpoilage2 = FoodSpoilage.of(itemStack2);

        if (foodSpoilage1 == null || !foodSpoilage1.isInitialized() || foodSpoilage2 == null || !foodSpoilage2.isInitialized()) {
            return original.call(instance, pOther);
        }

        Level level = NJSUtils.getLevelWithoutContext();

        FoodStatus foodStatus1 = FoodSpoilageManager.getFoodStatus(itemStack1, level);
        FoodStatus foodStatus2 = FoodSpoilageManager.getFoodStatus(itemStack2, level);

        if (foodStatus1 != foodStatus2) {
            return original.call(instance, pOther);
        }

        CompoundTag tagWithoutSpoilageTag1 = instance.copy();
        CompoundTag tagWithoutSpoilageTag2 = ((CompoundTag) pOther).copy();
        tagWithoutSpoilageTag1.remove("food_spoilage");
        tagWithoutSpoilageTag2.remove("food_spoilage");

        return original.call(tagWithoutSpoilageTag1, tagWithoutSpoilageTag2);
    }

    @Inject(method = "merge(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;min(II)I", ordinal = 1, shift = At.Shift.AFTER))
    private static void averageFoodLifetimeBeforeMerge(ItemStack destinationStack, ItemStack originStack, int amount, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 1) int i) {
        FoodSpoilageManager.updateFoodAndAverageFoodLifetimeBeforeMerge(destinationStack, originStack, i, NJSUtils.getLevelWithoutContext());
    }
}