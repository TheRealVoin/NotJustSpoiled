package net.therealvoin.notjustspoiled.foodspoilage;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.therealvoin.notjustspoiled.config.FoodCraftingMode;
import net.therealvoin.notjustspoiled.config.NJSServerConfig;
import net.therealvoin.notjustspoiled.network.DebugMessagePacket;
import net.therealvoin.notjustspoiled.util.NJSUtils;

public class FoodSpoilageManager {
    public static void changeEnvironmentAndUpdate(ItemStack itemStack, FoodEnvironment newFoodEnvironment, Level level) {
        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);

        if (foodSpoilage == null || foodSpoilage.getEnvironment() == newFoodEnvironment) {
            return;
        }

        updateFoodLifetime(itemStack, level);
        foodSpoilage.setEnvironment(newFoodEnvironment);

        if (!level.isClientSide()) {
            DebugMessagePacket.sendToAll(Component.literal("environment: " + newFoodEnvironment));
        }
    }

    public static void updateFoodLifetime(ItemStack itemStack, Level level) {
        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);

        if (foodSpoilage == null) {
            return;
        }

        if (!foodSpoilage.isInitialized()) {
            foodSpoilage.setLastUpdateTime(level.getGameTime());
            if (!level.isClientSide()) {
                DebugMessagePacket.sendToAll(Component.literal("Initialized food: " + ForgeRegistries.ITEMS.getKey(foodSpoilage.getItemStack().getItem()).toString() + "\nlastUpdateTime: " + level.getGameTime()));
            }
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

    public static void averageFoodLifetimeBeforeMerge(ItemStack destinationStack, ItemStack originStack, int movedAmount, Level level) {
        if (getFoodStatus(destinationStack, level) != getFoodStatus(originStack, level)) {
            return;
        }

        FoodSpoilage foodSpoilage1 = FoodSpoilage.of(destinationStack);
        FoodSpoilage foodSpoilage2 = FoodSpoilage.of(originStack);

        if (foodSpoilage1 == null || foodSpoilage2 == null) {
            return;
        }

        updateFoodLifetime(destinationStack, level);
        updateFoodLifetime(originStack, level);

        int destinationCount = destinationStack.getCount();

        double total = foodSpoilage1.getFoodLifetime() * destinationCount + foodSpoilage2.getFoodLifetime() * movedAmount;
        double average = total / (destinationCount + movedAmount);

        foodSpoilage1.setFoodLifetime(average);
        foodSpoilage2.setFoodLifetime(average);

        if (!level.isClientSide()) {
            DebugMessagePacket.sendToAll(Component.literal("Merged food: " + ForgeRegistries.ITEMS.getKey(destinationStack.getItem()).toString() + "\naverageFoodLifetime: " + average + "\ncount1: " + destinationCount + "\ncount2: " + movedAmount));
        }
    }

    public static double calculateActualFoodLifetime(FoodSpoilage foodSpoilage, Level level) {
        return (level.getGameTime() - foodSpoilage.getLastUpdateTime()) * foodSpoilage.getEnvironment().getFoodSpoilageMultiplier() + foodSpoilage.getFoodLifetime();
    }

    public static void copySpoilage(ItemStack copyFrom, ItemStack copyTo, Level level) {
        FoodSpoilage foodSpoilage1 = FoodSpoilage.of(copyFrom);
        FoodSpoilage foodSpoilage2 = FoodSpoilage.of(copyTo);

        if (foodSpoilage1 == null || foodSpoilage2 == null) {
            return;
        }

        updateFoodLifetime(copyFrom, level);
        double spoilagePercent = foodSpoilage1.getFoodLifetime() / FoodCategory.of(copyFrom).getSpoilageTime();
        foodSpoilage2.setFoodLifetime(spoilagePercent * FoodCategory.of(copyTo).getSpoilageTime());
        foodSpoilage2.setEnvironment(foodSpoilage1.getEnvironment());
        foodSpoilage2.setLastUpdateTime(foodSpoilage1.getLastUpdateTime());
    }

    public static void manageCrafting(ItemStack craftedItem, Container inventory) {
        FoodSpoilage resultStackSpoilage = FoodSpoilage.of(craftedItem);

        if (resultStackSpoilage == null) {
            return;
        }

        FoodCraftingMode craftingMode = NJSServerConfig.FOOD_CRAFTING_MODE.get();

        double totalSpoilagePercent = 0;
        int totalCount = 0;
        double worstPercent = 0;

        FoodStatus firstStatus = null;
        boolean hasMixedStatuses = false;
        Level level = NJSUtils.getLevelWithoutContext();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);

            FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);
            if (foodSpoilage == null) {
                continue;
            }

            double spoilagePercent = FoodSpoilageManager.calculateActualFoodLifetime(foodSpoilage, level) / FoodCategory.of(itemStack).getSpoilageTime();

            totalSpoilagePercent += spoilagePercent;
            worstPercent = Math.max(worstPercent, spoilagePercent);
            totalCount++;

            FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(itemStack, level);

            if (firstStatus == null) {
                firstStatus = foodStatus;
            } else if (firstStatus != foodStatus) {
                hasMixedStatuses = true;
            }
        }

        if (totalCount == 0) {
            return;
        }

        double finalSpoilagePercent = craftingMode.getCalculationMode(hasMixedStatuses) == FoodCraftingMode.CalculationMode.AVERAGE
                ? totalSpoilagePercent / totalCount
                : worstPercent;

        resultStackSpoilage.setFoodLifetime(finalSpoilagePercent * FoodCategory.of(craftedItem).getSpoilageTime());
        resultStackSpoilage.setEnvironment(FoodEnvironment.STORAGE);
        resultStackSpoilage.setLastUpdateTime(level.getGameTime());
    }

    public static boolean isCraftingAllowed(Container inventory, Level level) {
        FoodStatus firstStatus = null;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (FoodSpoilage.of(stack) == null) {
                continue;
            }

            FoodStatus status = FoodSpoilageManager.getFoodStatus(stack, level);
            if (firstStatus == null) {
                firstStatus = status;
                continue;
            }

            if (!NJSServerConfig.FOOD_CRAFTING_MODE.get().isAllowed(firstStatus, status)) {
                return false;
            }
        }

        return true;
    }
}