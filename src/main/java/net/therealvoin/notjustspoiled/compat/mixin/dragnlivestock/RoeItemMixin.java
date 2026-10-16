package net.therealvoin.notjustspoiled.compat.mixin.dragnlivestock;

import com.dragn0007.dragnlivestock.items.custom.CodRoeItem;
import com.dragn0007.dragnlivestock.items.custom.SalmonRoeItem;
import com.dragn0007.dragnpets.items.custom.TropicalFishRoeItem;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = {
        CodRoeItem.class,
        SalmonRoeItem.class,
        TropicalFishRoeItem.class
}, remap = false)
public abstract class RoeItemMixin {
    @Inject(method = "spawnFishFry", at = @At("HEAD"), cancellable = true)
    private void cancelFishSpawnIfFoodIsSpoiled(CallbackInfoReturnable<Optional<Mob>> cir, @Local(argsOnly = true) ServerLevel level, @Local(argsOnly = true) ItemStack itemInHand) {
        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemInHand);

        if (foodSpoilage == null) {
            return;
        }

        FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(itemInHand, level);
        if (foodStatus == FoodStatus.HALF_SPOILED || foodStatus == FoodStatus.SPOILED) {
            cir.setReturnValue(Optional.empty());
        }
    }
}