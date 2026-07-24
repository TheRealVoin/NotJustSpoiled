package net.therealvoin.notjustspoiled.mixin.minecraft.food;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {
    @ModifyArg(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"), index = 0)
    private int modifyFoodValuesAndApplyEffects(int defaultNutrition, @Local(argsOnly = true) ItemStack itemStack, @Local(argsOnly = true) LivingEntity entity) {
        Level level = entity == null ? NJSUtils.getLevelWithoutContext() : entity.level();

        if (level.isClientSide()) {
            return defaultNutrition;
        }

        FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(itemStack, level);
        if (foodStatus == null) {
            return defaultNutrition;
        }

        FoodSpoilageManager.updateFoodLifetime(itemStack, FoodEnvironment.INVENTORY, level);
        foodStatus.applyEffects(entity);

        return foodStatus.getModifiedNutrition(defaultNutrition);
    }
}