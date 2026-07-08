package net.therealvoin.notjustspoiled.mixin.minecraft.food;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {
    @ModifyArgs(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
    private void modifyFoodValuesAndApplyEffects(Args args, @Local(argsOnly = true) ItemStack itemStack, @Local(argsOnly = true) LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(itemStack, serverLevel);
            if (foodStatus == null) {
                return;
            }

            FoodSpoilageManager.updateFoodLifetime(itemStack, serverLevel);
            FoodProperties foodProperties = itemStack.getFoodProperties(entity);
            args.set(0, foodStatus.getModifiedNutrition(foodProperties.getNutrition()));
            args.set(1, foodStatus.getModifiedSaturation(foodProperties.getSaturationModifier()));

            foodStatus.applyEffects(entity);
        }
    }
}