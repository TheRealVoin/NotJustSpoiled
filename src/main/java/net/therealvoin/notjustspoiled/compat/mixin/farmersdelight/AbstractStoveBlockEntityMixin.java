package net.therealvoin.notjustspoiled.compat.mixin.farmersdelight;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;

@Mixin(AbstractStoveBlockEntity.class)
public class AbstractStoveBlockEntityMixin {
    @Inject(method = "cookAndOutputItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isItemEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z", shift = At.Shift.BEFORE))
    private void copySpoilageCapToCookedFood(CallbackInfo ci, @Local(name = "ingredient") ItemStack ingredient, @Local(name = "result") ItemStack result) {
        FoodSpoilageManager.copySpoilage(ingredient, result, ((AbstractStoveBlockEntity)(Object)this).getLevel());
    }

    @ModifyArg(method = "placeFood", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V"), index = 1, remap = false)
    private ItemStack updateFoodWhenPlacedOnStove(ItemStack stackToPlaceOnStove) {
        FoodEnvironment foodEnvironment = ((AbstractStoveBlockEntity)(Object)this).getBlockState().getValue(AbstractStoveBlock.LIT) ? FoodEnvironment.COOKING : FoodEnvironment.OPEN_AIR;
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPlaceOnStove, foodEnvironment, ((BlockEntity)(Object)this).getLevel());
        return stackToPlaceOnStove;
    }
}