package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraftforge.common.ForgeConfigSpec;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;

public enum FoodEnvironment {
    INVENTORY(NJSServerConfig.FOOD_SPOILAGE_IN_INVENTORY_MULTIPLIER),
    STORAGE(NJSServerConfig.FOOD_SPOILAGE_IN_STORAGE_MULTIPLIER),
    OPEN_AIR(NJSServerConfig.FOOD_SPOILAGE_ON_OPEN_AIR_MULTIPLIER);

    private final ForgeConfigSpec.DoubleValue foodSpoilageMultiplier;

    FoodEnvironment(ForgeConfigSpec.DoubleValue foodSpoilageMultiplier) {
        this.foodSpoilageMultiplier = foodSpoilageMultiplier;
    }

    public double getFoodSpoilageMultiplier() {
        return this.foodSpoilageMultiplier.get();
    }
}