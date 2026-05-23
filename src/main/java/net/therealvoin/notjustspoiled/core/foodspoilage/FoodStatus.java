package net.therealvoin.notjustspoiled.core.foodspoilage;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.ForgeConfigSpec;
import net.therealvoin.notjustspoiled.core.config.NJSServerConfig;

public enum FoodStatus {
    FRESH("fresh", ChatFormatting.GREEN, NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE),
    STALE("stale", ChatFormatting.YELLOW, NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE),
    HALF_SPOILED("half-spoiled", ChatFormatting.GOLD, NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_HALF_SPOILED_FOOD_IN_STORAGE),
    SPOILED("spoiled", ChatFormatting.RED, NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_SPOILED_FOOD_IN_STORAGE);

    private final String translationKey;
    private final ChatFormatting color;
    private final ForgeConfigSpec.DoubleValue chanceToAppearInStorage;

    FoodStatus(String translationKey, ChatFormatting color, ForgeConfigSpec.DoubleValue chanceToAppearInStorage) {
        this.translationKey = translationKey;
        this.color = color;
        this.chanceToAppearInStorage = chanceToAppearInStorage;
    }

    public Component getTranslation() {
        return Component.translatable("tooltip.notjustspoiled.status." + this.translationKey).withStyle(this.color);
    }

    public double getChanceToAppearInStorage() {
        return this.chanceToAppearInStorage.get();
    }

    public int getModifiedNutrition(int defaultNutrition) {
        switch (this) {
            case STALE -> {
                if (defaultNutrition <= 4) {
                    return defaultNutrition;
                } else if (defaultNutrition <= 10) {
                    return defaultNutrition - 1;
                } else if (defaultNutrition <= 14) {
                    return defaultNutrition - 2;
                } else if (defaultNutrition <= 22) {
                    return defaultNutrition - 3;
                } else {
                    return defaultNutrition - 4;
                }
            }

            case HALF_SPOILED -> {
                if (defaultNutrition == 1) {
                    return defaultNutrition;
                } else if (defaultNutrition <= 3) {
                    return defaultNutrition - 1;
                } else if (defaultNutrition <= 6) {
                    return defaultNutrition - 2;
                } else if (defaultNutrition <= 8) {
                    return defaultNutrition - 3;
                } else if (defaultNutrition <= 10) {
                    return defaultNutrition - 4;
                } else if (defaultNutrition <= 12) {
                    return defaultNutrition - 5;
                } else if (defaultNutrition <= 14) {
                    return defaultNutrition - 6;
                } else if (defaultNutrition <= 16) {
                    return defaultNutrition - 7;
                } else if (defaultNutrition <= 18) {
                    return defaultNutrition - 8;
                } else if (defaultNutrition <= 20) {
                    return defaultNutrition - 9;
                } else if (defaultNutrition <= 22) {
                    return defaultNutrition - 10;
                } else {
                    return defaultNutrition - 11;
                }
            }

            case SPOILED -> {
                if (defaultNutrition == 1) {
                    return defaultNutrition;
                } else if (defaultNutrition == 2) {
                    return defaultNutrition - 1;
                } else if (defaultNutrition == 3) {
                    return defaultNutrition - 2;
                } else if (defaultNutrition <= 5) {
                    return defaultNutrition - 3;
                } else if (defaultNutrition <= 7) {
                    return defaultNutrition - 4;
                } else if (defaultNutrition == 8) {
                    return defaultNutrition - 5;
                } else if (defaultNutrition == 9) {
                    return defaultNutrition - 6;
                } else if (defaultNutrition <= 11) {
                    return defaultNutrition - 7;
                } else if (defaultNutrition <= 13) {
                    return defaultNutrition - 8;
                } else if (defaultNutrition <= 15) {
                    return defaultNutrition - 9;
                } else if (defaultNutrition <= 17) {
                    return defaultNutrition - 10;
                } else if (defaultNutrition <= 22) {
                    return defaultNutrition - 12;
                } else {
                    return defaultNutrition - 13;
                }
            }
        }

        return defaultNutrition;
    }

    public float getModifiedSaturation(float defaultSaturation) {
        return defaultSaturation;
    }

    public static FoodStatus getFreshOrStaleStatus(RandomSource random) {
        double randomDouble = random.nextDouble();

        double fresh = NJSServerConfig.FRESH_OR_STALE$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE.get();

        if (randomDouble < fresh) {
            return FRESH;
        } else {
            return STALE;
        }
    }

    public static FoodStatus getRandomFoodStatus(RandomSource random) {
        double randomDouble = random.nextDouble();

        double fresh = FRESH.getChanceToAppearInStorage();
        double stale = STALE.getChanceToAppearInStorage();
        double halfSpoiled = HALF_SPOILED.getChanceToAppearInStorage();

        if (randomDouble < fresh) {
            return FRESH;
        } else if (randomDouble < fresh + stale) {
            return STALE;
        } else if (randomDouble < fresh + stale + halfSpoiled) {
            return HALF_SPOILED;
        } else {
            return SPOILED;
        }
    }
}