package net.therealvoin.notjustspoiled.compat.mixin.survival_instinct;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.mcreator.survivalinstinct.item.OrangeItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OrangeItem.class)
public abstract class OrangeItemMixin {
    @WrapOperation(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/mcreator/survivalinstinct/procedures/PlayerEatOrangeProcedure;execute(Lnet/minecraft/world/entity/Entity;)V"))
    private void cancelRemovingEffectsIfOrangeIsHalfSpoiledOrSpoiled(Entity entity, Operation<Void> originalMethod, @Local(argsOnly = true) ItemStack itemStack) {
        FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(itemStack, entity.level());
        if (foodStatus != FoodStatus.HALF_SPOILED && foodStatus != FoodStatus.SPOILED) {
            originalMethod.call(entity);
        }
    }
}