package net.therealvoin.notjustspoiled.util;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.therealvoin.notjustspoiled.core.config.NJSConfig;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.core.foodspoilage.capability.foodspoilage.FoodSpoilageProvider;
import net.therealvoin.notjustspoiled.core.foodspoilage.capability.foodspoilage.IFoodSpoilage;

public class NJSUtils {
    public static Level getLevelBySlot(Slot slot) {
        Container container = slot.container;
        if (container instanceof Inventory inventory) {
            return inventory.player.level();
        } else if (container instanceof BaseContainerBlockEntity blockEntity) {
            return blockEntity.getLevel();
        } else if (container instanceof AbstractMinecartContainer minecart) {
            return minecart.level();
        } else if (container instanceof ChestBoat chestBoat) {
            return chestBoat.level();
        } else if (container instanceof SimpleContainer simpleContainer) {
            AbstractHorse horse = ((SimpleContainerAccessor) simpleContainer).getHorse();
            if (horse != null) {
                return horse.level();
            }
        }

        return null;
    }

    public static IFoodSpoilage getCapability(ItemStack itemStack) {
        if (FoodCategory.getFoodCategory(itemStack) == null) {
            return null;
        }

        return itemStack.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).resolve().orElse(null);
    }

    public static void copyCapability(ItemStack copyFrom, ItemStack copyTo) {
        copyFrom.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage1 -> {
            copyTo.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage2 -> {
                double spoilagePercent = foodSpoilage1.getFoodLifetime() / FoodCategory.getFoodCategory(copyFrom).getSpoilageTime();
                foodSpoilage2.setFoodLifetime(spoilagePercent * FoodCategory.getFoodCategory(copyTo).getSpoilageTime());
                foodSpoilage2.setEnvironment(foodSpoilage1.getEnvironment());
                foodSpoilage2.setLastUpdateTime(foodSpoilage1.getLastUpdateTime());
            });
        });
    }

    public static void validateChances(ModConfigEvent event) {
        if (event.getConfig().getSpec() != NJSConfig.CONFIG) {
            return;
        }

        double fresh = NJSConfig.CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE.get();
        double stale = NJSConfig.CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE.get();
        double halfSpoiled = NJSConfig.CHANCE_TO_APPEAR_HALF_SPOILED_FOOD_IN_STORAGE.get();
        double spoiled = NJSConfig.CHANCE_TO_APPEAR_SPOILED_FOOD_IN_STORAGE.get();
        double remaining = 1.0;

        fresh = Math.min(fresh, remaining);
        remaining -= fresh;
        stale = Math.min(stale, remaining);
        remaining -= stale;
        halfSpoiled = Math.min(halfSpoiled, remaining);
        remaining -= halfSpoiled;
        spoiled = Math.min(spoiled, remaining);
        remaining -= spoiled;

        if (remaining > 0) {
            fresh += remaining;
        }

        NJSConfig.CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE.set(fresh);
        NJSConfig.CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE.set(stale);
        NJSConfig.CHANCE_TO_APPEAR_HALF_SPOILED_FOOD_IN_STORAGE.set(halfSpoiled);
        NJSConfig.CHANCE_TO_APPEAR_SPOILED_FOOD_IN_STORAGE.set(spoiled);
    }
}