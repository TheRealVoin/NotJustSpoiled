package net.therealvoin.notjustspoiled.compat.mixin.farmersdelight;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;

@Mixin(AbstractStoveBlockEntity.class)
public abstract class AbstractStoveBlockEntityMixin {
    @Inject(method = "cookAndOutputItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isItemEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z", shift = At.Shift.BEFORE))
    private void copySpoilageToCookedFood(CallbackInfo ci, @Local(name = "ingredient") ItemStack ingredient, @Local(name = "result") ItemStack result) {
        FoodSpoilageManager.copySpoilage(ingredient, FoodEnvironment.OPEN_AIR, result, ((AbstractStoveBlockEntity)(Object)this).getLevel());
    }

    @Inject(method = "serverTick", at = @At("TAIL"), remap = false)
    private static void validateFoodEnvironment(CallbackInfo ci, @Local(argsOnly = true) AbstractStoveBlockEntity blockEntity) {
        ItemStackHandler items = blockEntity.getItems();
        for (int i = 0; i < items.getSlots(); i++) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(items.getStackInSlot(i), FoodEnvironment.OPEN_AIR, blockEntity.getLevel());
        }
    }
}