package net.therealvoin.notjustspoiled.compat.mixin.alexsmobs;

import com.github.alexthe666.alexsmobs.block.BlockCapsid;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockCapsid.class)
public abstract class BlockCapsidMixin {
    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"))
    private void mergeFood(ItemStack stackInCapsid, int increment, Operation<Void> originalMethod, @Local(argsOnly = true) Level level, @Local(name = "copy") ItemStack stackInHand) {
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInCapsid, FoodEnvironment.STORAGE, stackInHand, FoodEnvironment.INVENTORY, increment, level);

        originalMethod.call(stackInCapsid, increment);
    }

    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean removeSpoilageTagFromEqualityCheck(ItemStack stackInCapsid, ItemStack stackInHand, Operation<Boolean> originalMethod, @Local(argsOnly = true) Level level) {
        FoodSpoilage stackInCapsidSpoilage = FoodSpoilage.of(stackInCapsid);
        FoodSpoilage stackInHandSpoilage = FoodSpoilage.of(stackInHand);

        if (stackInCapsidSpoilage == null || stackInHandSpoilage == null) {
            return originalMethod.call(stackInCapsid, stackInHand);
        }

        FoodStatus stackInCapsidStatus = FoodSpoilageManager.getFoodStatus(stackInCapsid, level);
        FoodStatus stackInHandStatus = FoodSpoilageManager.getFoodStatus(stackInHand, level);

        if (stackInCapsidStatus != stackInHandStatus) {
            return false;
        }

        return originalMethod.call(stackInCapsid, stackInHand);
    }
}