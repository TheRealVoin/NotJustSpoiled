package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraftforge.common.ForgeConfigSpec;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;

public enum FoodEnvironment {
    NONE(null),
    INVENTORY(NJSServerConfig.FOOD_SPOILAGE_IN_INVENTORY_MULTIPLIER),
    STORAGE(NJSServerConfig.FOOD_SPOILAGE_IN_STORAGE_MULTIPLIER),
    GROUND(NJSServerConfig.FOOD_SPOILAGE_ON_GROUND_MULTIPLIER),
    COOKING(NJSServerConfig.FOOD_SPOILAGE_WHILE_COOKING_MULTIPLIER);

    private final ForgeConfigSpec.DoubleValue foodSpoilageMultiplier;

    FoodEnvironment(ForgeConfigSpec.DoubleValue foodSpoilageMultiplier) {
        this.foodSpoilageMultiplier = foodSpoilageMultiplier;
    }

    public double getFoodSpoilageMultiplier() {
        return this.foodSpoilageMultiplier.get();
    }
}