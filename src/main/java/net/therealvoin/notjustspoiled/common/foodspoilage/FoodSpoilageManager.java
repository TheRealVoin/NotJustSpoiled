package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.foodspoilage.FoodSpoilageProvider;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.foodspoilage.IFoodSpoilage;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;

import java.util.List;

public class FoodSpoilageManager {
    private static final Component DEBUG1 = Component.translatable("message.notjustspoiled.debug.food_updated").append("\nlastUpdateTime: %d\nfoodLifetime: %f\ngameTime: %d");
    private static final Component DEBUG2 = Component.literal("environment: %s");

    public static void changeEnvironmentAndUpdate(ItemStack itemStack, FoodEnvironment newFoodEnvironment, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        itemStack.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
            if (foodSpoilage.getEnvironment() == newFoodEnvironment) {
                return;
            }

            updateFoodLifetime(foodSpoilage, serverLevel);
            foodSpoilage.setEnvironment(newFoodEnvironment);

            if (NJSServerConfig.SHOW_DEBUG_MESSAGE.get()) {
                List<ServerPlayer> players = serverLevel.getServer().getPlayerList().getPlayers();
                if (!players.isEmpty()) {
                    players.get(0).sendSystemMessage(Component.literal(String.format(DEBUG2.getString(), foodSpoilage.getEnvironment().name())));
                }
            }
        });
    }

    public static void updateFoodLifetime(IFoodSpoilage foodSpoilage, ServerLevel serverLevel) {
        long gameTime = serverLevel.getGameTime();

        if (!isInitialized(foodSpoilage)) {
            foodSpoilage.setLastUpdateTime(gameTime);
            return;
        }

        if (foodSpoilage.getLastUpdateTime() == gameTime) {
            return;
        }

        foodSpoilage.setFoodLifetime(calculateActualFoodLifetime(gameTime, foodSpoilage.getLastUpdateTime(), foodSpoilage.getEnvironment().getFoodSpoilageMultiplier(), foodSpoilage.getFoodLifetime()));
        foodSpoilage.setLastUpdateTime(gameTime);

        if (NJSServerConfig.SHOW_DEBUG_MESSAGE.get()) {
            List<ServerPlayer> players = serverLevel.getServer().getPlayerList().getPlayers();
            if (!players.isEmpty()) {
                players.get(0).sendSystemMessage(Component.literal(String.format(DEBUG1.getString(), foodSpoilage.getLastUpdateTime(), foodSpoilage.getFoodLifetime(), gameTime)));
            }
        }
    }

    public static FoodStatus getFoodStatus(ItemStack itemStack, Level level) {
        IFoodSpoilage foodSpoilage = NJSUtils.getCapability(itemStack);
        if (!isInitialized(foodSpoilage)) {
            return null;
        }

        double foodLifetime = calculateActualFoodLifetime(level.getGameTime(), foodSpoilage.getLastUpdateTime(), foodSpoilage.getEnvironment().getFoodSpoilageMultiplier(), foodSpoilage.getFoodLifetime());
        return getFoodStatus(foodLifetime, FoodCategory.getFoodCategory(itemStack).getSpoilageTime());
    }

    public static void tryAverageSpoilageOnMerge(ItemStack itemStack1, ItemStack itemStack2, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (itemStack1.isEmpty() || itemStack2.isEmpty() || !itemStack1.is(itemStack2.getItem())) {
            return;
        }

        itemStack1.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage1 -> {
            itemStack2.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage2 -> {
                if (getFoodStatus(itemStack1, serverLevel) != getFoodStatus(itemStack2, serverLevel)) {
                    return;
                }

                updateFoodLifetime(foodSpoilage1, serverLevel);
                updateFoodLifetime(foodSpoilage2, serverLevel);

                int count1 = itemStack1.getCount();
                int count2 = itemStack2.getCount();
                double total = foodSpoilage1.getFoodLifetime() * count1 + foodSpoilage2.getFoodLifetime() * count2;
                double average = total / (count1 + count2);

                foodSpoilage1.setFoodLifetime(average);
                foodSpoilage2.setFoodLifetime(average);
            });
        });
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
        if (foodLifetime < spoilageTime / 3.0) {
            return FoodStatus.FRESH;
        } else if (foodLifetime < spoilageTime / 3.0 * 2) {
            return FoodStatus.STALE;
        } else if (foodLifetime < spoilageTime) {
            return FoodStatus.HALF_SPOILED;
        } else {
            return FoodStatus.SPOILED;
        }
    }

    public static double calculateActualFoodLifetime(long gameTime, long lastUpdateTime, double spoilageMultiplier, double foodLifetime) {
        return (gameTime - lastUpdateTime) * spoilageMultiplier + foodLifetime;
    }

    public static boolean isInitialized(ItemStack itemStack) {
        return isInitialized(NJSUtils.getCapability(itemStack));
    }

    public static boolean isInitialized(IFoodSpoilage foodSpoilage) {
        return foodSpoilage != null && foodSpoilage.getLastUpdateTime() != 0;
    }

    public static boolean isInitialized(long lastUpdateTime) {
        return lastUpdateTime != 0;
    }
}