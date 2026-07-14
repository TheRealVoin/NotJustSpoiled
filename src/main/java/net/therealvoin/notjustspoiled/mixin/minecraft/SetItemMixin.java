package net.therealvoin.notjustspoiled.mixin.minecraft;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import net.therealvoin.notjustspoiled.common.util.SimpleContainerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RandomizableContainerBlockEntity.class, ChestBoat.class, AbstractMinecartContainer.class, SimpleContainer.class, AbstractFurnaceBlockEntity.class, Inventory.class})
public abstract class SetItemMixin {
    @Inject(method = "setItem", at = @At("TAIL"))
    private void changeFoodEnvironmentWhenPlacedInContainer(int index, ItemStack itemStack, CallbackInfo ci) {
//        if (itemStack.isEmpty()) {
//            return;
//        }

        Object object = this;
        Level level = null;
        FoodEnvironment foodEnvironment = null;

        if (object instanceof Inventory inventory) {
            level = inventory.player.level();
            foodEnvironment = FoodEnvironment.INVENTORY;
        } else if (object instanceof AbstractMinecartContainer minecartContainer) {
            level = minecartContainer.level();
            foodEnvironment = FoodEnvironment.STORAGE;
        } else if (object instanceof BaseContainerBlockEntity blockEntity) {
            level = blockEntity.getLevel();
            foodEnvironment = FoodEnvironment.STORAGE;
        } else if (object instanceof SimpleContainer simpleContainer) {
            AbstractHorse horse = ((SimpleContainerAccessor) simpleContainer).getHorse();
            if (horse != null) {
                level = horse.level();
                foodEnvironment = FoodEnvironment.STORAGE;
            }
        } else if (object instanceof ChestBoat chestBoat) {
            level = chestBoat.level();
            foodEnvironment = FoodEnvironment.STORAGE;
        }

        FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, foodEnvironment, level);

        if (level instanceof ServerLevel serverLevel) {
            FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);

            if (foodSpoilage != null) {
                if (foodSpoilage.getFoodLifetime() == 0 && foodSpoilage.getEnvironment() != FoodEnvironment.INVENTORY) {
                    NJSUtils.sendWarningMessage(serverLevel);
                }
            }
        }
    }
}