package net.therealvoin.notjustspoiled.mixin.minecraft.block;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {
    @ModifyArg(method = "placeFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private Object changeFoodEnvironmentWhenPlacedOnCampfire(Object value) {
        ItemStack stackToPlaceOnCampfire = (ItemStack) value;
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPlaceOnCampfire, FoodEnvironment.OPEN_AIR, ((CampfireBlockEntity)(Object)this).getLevel());
        return stackToPlaceOnCampfire;
    }
}