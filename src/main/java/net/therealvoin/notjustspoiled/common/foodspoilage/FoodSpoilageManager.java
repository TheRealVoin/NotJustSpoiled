package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.therealvoin.notjustspoiled.common.network.DebugMessagePacket;

public class FoodSpoilageManager {
    public static void changeEnvironmentAndUpdate(ItemStack itemStack, FoodEnvironment newFoodEnvironment, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);

        if (foodSpoilage == null || foodSpoilage.getEnvironment() == newFoodEnvironment) {
            return;
        }

        updateFoodLifetime(itemStack, serverLevel);
        foodSpoilage.setEnvironment(newFoodEnvironment);

        DebugMessagePacket.sendToAll(Component.literal("environment: " + newFoodEnvironment));
    }

    public static void updateFoodLifetime(ItemStack itemStack, ServerLevel serverLevel) {
        long gameTime = serverLevel.getGameTime();

        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);

        if (!isInitialized(foodSpoilage)) {
            foodSpoilage.setLastUpdateTime(gameTime);
            DebugMessagePacket.sendToAll(Component.literal("Initialized food: " + ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString() + "\nlastUpdateTime: " + gameTime));
            return;
        }

        if (foodSpoilage.getLastUpdateTime() == gameTime) {
            return;
        }

        foodSpoilage.setFoodLifetime(calculateActualFoodLifetime(foodSpoilage, serverLevel));
        foodSpoilage.setLastUpdateTime(gameTime);

        DebugMessagePacket.sendToAll(Component.literal("Updated food: " + ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString() + "\nfoodLifetime: " + foodSpoilage.getFoodLifetime() + "\nlastUpdateTime: " + gameTime));
    }

    public static FoodStatus getFoodStatus(ItemStack itemStack, Level level) {
        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);
        if (!isInitialized(foodSpoilage)) {
            return null;
        }

        double foodLifetime = calculateActualFoodLifetime(foodSpoilage, level);
        return getFoodStatus(foodLifetime, FoodCategory.getFoodCategory(itemStack).getSpoilageTime());
    }

    public static void tryAverageSpoilageOnMerge(ItemStack itemStack1, ItemStack itemStack2, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (itemStack1.isEmpty() || itemStack2.isEmpty() || !itemStack1.is(itemStack2.getItem())) {
            return;
        }

        if (getFoodStatus(itemStack1, serverLevel) != getFoodStatus(itemStack2, serverLevel)) {
            return;
        }

        FoodSpoilage foodSpoilage1 = FoodSpoilage.of(itemStack1);
        FoodSpoilage foodSpoilage2 = FoodSpoilage.of(itemStack2);

        if (foodSpoilage1 == null || foodSpoilage2 == null) {
            return;
        }

        updateFoodLifetime(itemStack1, serverLevel);
        updateFoodLifetime(itemStack2, serverLevel);

        int count1 = itemStack1.getCount();
        int count2 = itemStack2.getCount();
        double total = foodSpoilage1.getFoodLifetime() * count1 + foodSpoilage2.getFoodLifetime() * count2;
        double average = total / (count1 + count2);

        foodSpoilage1.setFoodLifetime(average);
        foodSpoilage2.setFoodLifetime(average);
    }

    public static int getRandomFoodLifetime(FoodCategory foodCategory, RandomSource random, boolean includeAll) {
        int spoilageTime = foodCategory.getSpoilageTime();
        FoodStatus foodStatus;

        if (includeAll) {
            foodStatus = FoodStatus.getRandomFoodStatus(random);
        } else {
            foodStatus = FoodStatus.getFreshOrStaleStatus(random);
        }


        switch (foodStatus) {
            case FRESH -> {
                return random.nextIntBetweenInclusive(0, spoilageTime / 3 - 1);
            }
            case STALE -> {
                return random.nextIntBetweenInclusive(spoilageTime / 3, spoilageTime / 3 * 2 - 1);
            }
            case HALF_SPOILED -> {
                return random.nextIntBetweenInclusive(spoilageTime / 3 * 2, spoilageTime - 1);
            }
            case SPOILED -> {
                return spoilageTime;
            }
            default -> {
                return -1;
            }
        }
    }

    public static FoodStatus getFoodStatus(double foodLifetime, int spoilageTime) {
        if (foodLifetime < FoodStatus.FRESH.getThreshold(spoilageTime)) {
            return FoodStatus.FRESH;
        } else if (foodLifetime < FoodStatus.STALE.getThreshold(spoilageTime)) {
            return FoodStatus.STALE;
        } else if (foodLifetime < FoodStatus.HALF_SPOILED.getThreshold(spoilageTime)) {
            return FoodStatus.HALF_SPOILED;
        } else {
            return FoodStatus.SPOILED;
        }
    }

    public static double calculateActualFoodLifetime(FoodSpoilage foodSpoilage, Level level) {
        return (level.getGameTime() - foodSpoilage.getLastUpdateTime()) * foodSpoilage.getEnvironment().getFoodSpoilageMultiplier() + foodSpoilage.getFoodLifetime();
    }

    public static boolean isInitialized(ItemStack itemStack) {
        return isInitialized(FoodSpoilage.of(itemStack));
    }

    public static boolean isInitialized(FoodSpoilage foodSpoilage) {
        return foodSpoilage != null && foodSpoilage.getLastUpdateTime() != -1;
    }
}