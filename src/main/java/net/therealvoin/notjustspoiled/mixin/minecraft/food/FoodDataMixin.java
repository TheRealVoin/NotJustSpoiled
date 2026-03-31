package net.therealvoin.notjustspoiled.mixin.minecraft.food;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {
    @Shadow public abstract void eat(int pFoodLevelModifier, float pSaturationLevelModifier);

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V", shift = At.Shift.BEFORE), cancellable = true)
    private void modifyFoodNutrition(Item item, ItemStack stack, LivingEntity entity, CallbackInfo ci) {
        FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(stack, entity.level());
        if (foodStatus == null) {
            return;
        }

        FoodProperties foodProperties = stack.getFoodProperties(entity);
        this.eat(foodStatus.getModifiedNutrition(foodProperties.getNutrition()), foodStatus.getModifiedSaturation(foodProperties.getSaturationModifier()));
        ci.cancel();
    }
}