package net.therealvoin.notjustspoiled.core.foodspoilage;

import net.minecraftforge.common.ForgeConfigSpec;
import net.therealvoin.notjustspoiled.core.config.NJSConfig;

public enum FoodEnvironment {
    INVENTORY(NJSConfig.FOOD_SPOILAGE_IN_INVENTORY_MULTIPLIER),
    STORAGE(NJSConfig.FOOD_SPOILAGE_IN_STORAGE_MULTIPLIER),
    GROUND(NJSConfig.FOOD_SPOILAGE_ON_GROUND_MULTIPLIER),
    COOKING(NJSConfig.FOOD_SPOILAGE_WHILE_COOKING_MULTIPLIER);

    private final ForgeConfigSpec.DoubleValue foodSpoilageMultiplier;

    FoodEnvironment(ForgeConfigSpec.DoubleValue foodSpoilageMultiplier) {
        this.foodSpoilageMultiplier = foodSpoilageMultiplier;
    }

    public double getFoodSpoilageMultiplier() {
        return this.foodSpoilageMultiplier.get();
    }
}