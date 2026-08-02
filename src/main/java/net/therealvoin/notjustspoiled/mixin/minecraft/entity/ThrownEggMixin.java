package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownEgg.class)
public abstract class ThrownEggMixin {
    @WrapOperation(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean cancelChickenSpawnIfFoodIsSpoiled(Level level, Entity entity, Operation<Boolean> originalMethod) {
        ItemStack itemStack = ((ThrownEgg)(Object)this).getItem();
        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);

        if (foodSpoilage == null) {
            return originalMethod.call(level, entity);
        }

        FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(itemStack, level);
        if (foodStatus == FoodStatus.HALF_SPOILED || foodStatus == FoodStatus.SPOILED) {
            return false;
        }

        return originalMethod.call(level, entity);
    }
}