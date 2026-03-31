package net.therealvoin.notjustspoiled.mixin.mods;

import dev.enemeez.simplefarming.common.block.entity.FermenterBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {FermenterBlockEntity.class}, remap = false)
public abstract class SetItemMixin {
    @Inject(method = "setItem", at = @At("HEAD"))
    private void changeFoodEnvironmentWhenPlacedInContainer(int index, ItemStack itemStack, CallbackInfo ci) {
        Object object = this;
        Level level = null;
        FoodEnvironment foodEnvironment = null;

        if (object instanceof FermenterBlockEntity blockEntity) {
            level = blockEntity.getLevel();
            foodEnvironment = FoodEnvironment.STORAGE;
        }

        if (level == null || level.isClientSide() || itemStack.isEmpty()) {
            return;
        }

        FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, foodEnvironment, level);
    }
}