package net.therealvoin.notjustspoiled.mixin.mods;

import dev.enemeez.simplefarming.common.block.entity.FermenterBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.entity.BasketBlockEntity;

@Mixin(value = {BasketBlockEntity.class, FermenterBlockEntity.class}, remap = false)
public abstract class SetItemMixin {
    @Inject(method = {"setItem", "m_6836_"}, at = @At("HEAD"))
    private void changeFoodEnvironmentWhenPlacedInContainer(int index, ItemStack itemStack, CallbackInfo ci) {
        BlockEntity blockEntity = (BlockEntity) (Object) this;
        Level level = blockEntity.getLevel();
        FoodEnvironment foodEnvironment = FoodEnvironment.STORAGE;

        if (level instanceof ServerLevel serverLevel && !itemStack.isEmpty()) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, foodEnvironment, serverLevel);
        }
    }
}