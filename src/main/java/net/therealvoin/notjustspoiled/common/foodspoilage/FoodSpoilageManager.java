package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.therealvoin.notjustspoiled.common.network.DebugMessagePacket;

public class FoodSpoilageManager {
    public static void changeEnvironmentAndUpdate(ItemStack itemStack, FoodEnvironment newFoodEnvironment, Level level) {
        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);

        if (foodSpoilage == null || foodSpoilage.getEnvironment() == newFoodEnvironment) {
            return;
        }

        FoodEnvironment currentEnvironment = foodSpoilage.isInitialized() ? foodSpoilage.getEnvironment() : newFoodEnvironment;

        updateFoodLifetime(itemStack, currentEnvironment, level);
        foodSpoilage.setEnvironment(newFoodEnvironment);


        if (!level.isClientSide()) {
            DebugMessagePacket.sendToAll(Component.literal("environment: " + newFoodEnvironment));
        }
    }

    public static void updateFoodLifetime(ItemStack itemStack, FoodEnvironment currentEnvironment, Level level) {
        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);

        if (foodSpoilage == null) {
            return;
        }

        if (!foodSpoilage.isInitialized()) {
            initializeFood(foodSpoilage, currentEnvironment, level);
            return;
        }

        long gameTime = level.getGameTime();

        if (foodSpoilage.getLastUpdateTime() == gameTime) {
            return;
        }

        foodSpoilage.setFoodLifetime(calculateActualFoodLifetime(foodSpoilage, level));
        foodSpoilage.setLastUpdateTime(gameTime);

        if (!level.isClientSide()) {
            DebugMessagePacket.sendToAll(Component.literal("Updated food: " + ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString() + "\nfoodLifetime: " + foodSpoilage.getFoodLifetime() + "\nlastUpdateTime: " + gameTime));
        }
    }

    public static FoodStatus getFoodStatus(ItemStack itemStack, Level level) {
        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);
        if (foodSpoilage == null) {
            return null;
        }

        if (!foodSpoilage.isInitialized()) {
            return FoodStatus.FRESH;
        }

        double foodLifetime = calculateActualFoodLifetime(foodSpoilage, level);
        int spoilageTime = FoodCategory.of(itemStack).getSpoilageTime();

        for (FoodStatus foodStatus : FoodStatus.values()) {
            if (foodLifetime < foodStatus.getEnd(spoilageTime)) {
                return foodStatus;
            }
        }

        return FoodStatus.SPOILED;
    }

    public static void averageFoodLifetimeBeforeMerge(ItemStack destinationStack, FoodEnvironment destinationEnvironment, ItemStack originStack, FoodEnvironment originEnvironment, int movedAmount, Level level) {
        if (getFoodStatus(destinationStack, level) != getFoodStatus(originStack, level)) {
            return;
        }

        FoodSpoilage foodSpoilage1 = FoodSpoilage.of(destinationStack);
        FoodSpoilage foodSpoilage2 = FoodSpoilage.of(originStack);

        if (foodSpoilage1 == null || foodSpoilage2 == null) {
            return;
        }

        updateFoodLifetime(destinationStack, destinationEnvironment, level);
        updateFoodLifetime(originStack, originEnvironment, level);

        int destinationCount = destinationStack.getCount();

        double total = foodSpoilage1.getFoodLifetime() * destinationCount + foodSpoilage2.getFoodLifetime() * movedAmount;
        double average = total / (destinationCount + movedAmount);

        foodSpoilage1.setFoodLifetime(average);
        foodSpoilage2.setFoodLifetime(average);

        DebugMessagePacket.sendToAll(Component.literal("Merged food: " + ForgeRegistries.ITEMS.getKey(destinationStack.getItem()).toString() + "\naverageFoodLifetime: " + average + "\ncount1: " + destinationCount + "\ncount2: " + movedAmount));
    }

    public static double calculateActualFoodLifetime(FoodSpoilage foodSpoilage, Level level) {
        return (level.getGameTime() - foodSpoilage.getLastUpdateTime()) * foodSpoilage.getEnvironment().getFoodSpoilageMultiplier() + foodSpoilage.getFoodLifetime();
    }

    public static void copySpoilage(ItemStack copyFrom, FoodEnvironment copyFromEnvironment, ItemStack copyTo, Level level) {
        FoodSpoilage foodSpoilage1 = FoodSpoilage.of(copyFrom);
        FoodSpoilage foodSpoilage2 = FoodSpoilage.of(copyTo);

        if (foodSpoilage1 == null || foodSpoilage2 == null) {
            return;
        }

        updateFoodLifetime(copyFrom, copyFromEnvironment, level);
        double spoilagePercent = foodSpoilage1.getFoodLifetime() / FoodCategory.of(copyFrom).getSpoilageTime();
        foodSpoilage2.setFoodLifetime(spoilagePercent * FoodCategory.of(copyTo).getSpoilageTime());
        foodSpoilage2.setEnvironment(foodSpoilage1.getEnvironment());
        foodSpoilage2.setLastUpdateTime(foodSpoilage1.getLastUpdateTime());
    }

    private static void initializeFood(FoodSpoilage foodSpoilage, FoodEnvironment foodEnvironment, Level level) {
        foodSpoilage.setEnvironment(foodEnvironment);
        foodSpoilage.setLastUpdateTime(level.getGameTime());
        if (!level.isClientSide()) {
            DebugMessagePacket.sendToAll(Component.literal("Initialized food: " + ForgeRegistries.ITEMS.getKey(foodSpoilage.getItemStack().getItem()).toString() + "\nlastUpdateTime: " + level.getGameTime()));
        }
    }
}