package net.therealvoin.notjustspoiled.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NJSServerConfig {
    public static final ForgeConfigSpec CONFIG;

    public static final ForgeConfigSpec.DoubleValue FOOD_SPOILAGE_IN_INVENTORY_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FOOD_SPOILAGE_IN_STORAGE_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FOOD_SPOILAGE_ON_OPEN_AIR_MULTIPLIER;

    public static final ForgeConfigSpec.DoubleValue FRESH_OR_STALE$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE;
    public static final ForgeConfigSpec.DoubleValue FRESH_OR_STALE$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE;

    public static final ForgeConfigSpec.DoubleValue RANDOM$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE;
    public static final ForgeConfigSpec.DoubleValue RANDOM$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE;
    public static final ForgeConfigSpec.DoubleValue RANDOM$CHANCE_TO_APPEAR_HALF_SPOILED_FOOD_IN_STORAGE;
    public static final ForgeConfigSpec.DoubleValue RANDOM$CHANCE_TO_APPEAR_SPOILED_FOOD_IN_STORAGE;

    public static final ForgeConfigSpec.EnumValue<FoodCraftingMode> FOOD_CRAFTING_MODE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.translation("config.notjustspoiled.food_spoilage_multiplier").push("Food Spoilage Multiplier");
        FOOD_SPOILAGE_IN_INVENTORY_MULTIPLIER = builder
                .comment("A modifier that affects the speed of food spoiling in inventory.")
                .translation("config.notjustspoiled.food_spoilage_multiplier.inventory")
                .worldRestart()
                .defineInRange("foodSpoilageInInventoryMultiplier", 1, 0.01, 100);

        FOOD_SPOILAGE_IN_STORAGE_MULTIPLIER = builder
                .comment("A modifier that affects the speed of food spoiling in storage.")
                .translation("config.notjustspoiled.food_spoilage_multiplier.storage")
                .worldRestart()
                .defineInRange("foodSpoilageInStorageMultiplier", 0.7, 0.01, 100);

        FOOD_SPOILAGE_ON_OPEN_AIR_MULTIPLIER = builder
                .comment("A modifier that affects the speed of food spoiling on open air.")
                .translation("config.notjustspoiled.food_spoilage_multiplier.open_air")
                .worldRestart()
                .defineInRange("foodSpoilageOnOpenAirMultiplier", 2, 0.01, 100);
        builder.pop();

        builder.translation("config.notjustspoiled.chance_to_appear_food_in_storage").push("Chance to appear food in storage");
        builder.translation("config.notjustspoiled.chance_to_appear_food_in_storage.fresh_or_stale").push("Fresh or stale");
        FRESH_OR_STALE$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE = builder
                .comment(
                        "Chance of fresh food to appear in storage.",
                        "Note: all values must add up to 1."
                )
                .translation("config.notjustspoiled.chance_to_appear_food_in_storage.fresh")
                .worldRestart()
                .defineInRange("chanceToAppearFreshFoodInStorage", 0.5, 0, 1);

        FRESH_OR_STALE$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE = builder
                .comment(
                        "Chance of stale food to appear in storage.",
                        "Note: all values must add up to 1."
                )
                .translation("config.notjustspoiled.chance_to_appear_food_in_storage.stale")
                .worldRestart()
                .defineInRange("chanceToAppearStaleFoodInStorage", 0.5, 0, 1);
        builder.pop();

        builder.translation("config.notjustspoiled.chance_to_appear_food_in_storage.random").push("Random");
        RANDOM$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE = builder
                .comment(
                        "Chance of fresh food to appear in storage.",
                        "Note: all values must add up to 1."
                )
                .translation("config.notjustspoiled.chance_to_appear_food_in_storage.fresh")
                .worldRestart()
                .defineInRange("chanceToAppearFreshFoodInStorage", 0.1, 0, 1);
        RANDOM$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE = builder
                .comment(
                        "Chance of stale food to appear in storage.",
                        "Note: all values must add up to 1."
                )
                .translation("config.notjustspoiled.chance_to_appear_food_in_storage.stale")
                .worldRestart()
                .defineInRange("chanceToAppearStaleFoodInStorage", 0.2, 0, 1);
        RANDOM$CHANCE_TO_APPEAR_HALF_SPOILED_FOOD_IN_STORAGE = builder
                .comment(
                        "Chance of half-spoiled food to appear in storage.",
                        "Note: all values must add up to 1."
                )
                .translation("config.notjustspoiled.chance_to_appear_food_in_storage.half-spoiled")
                .worldRestart()
                .defineInRange("chanceToAppearHalfSpoiledFoodInStorage", 0.3, 0, 1);
        RANDOM$CHANCE_TO_APPEAR_SPOILED_FOOD_IN_STORAGE = builder
                .comment(
                        "Chance of spoiled food to appear in storage.",
                        "Note: all values must add up to 1."
                )
                .translation("config.notjustspoiled.chance_to_appear_food_in_storage.spoiled")
                .worldRestart()
                .defineInRange("chanceToAppearSpoiledFoodInStorage", 0.4, 0, 1);
        builder.pop();
        builder.pop();

        FOOD_CRAFTING_MODE = builder
                .comment(
                        "Correctly calculates the spoilage time of a crafted food.",
                        "AVERAGE: the spoilage time is defined as an arithmetic mean of the spoilage time of all ingredients.",
                        "SAME_STATUS: the food can be crafted only when all ingredients have the same status.",
                        "WORST_STATUS: the spoilage time is determined by the worst ingredient, ignoring others.",
                        "FRESH_ONLY: the food can be crafted only when all ingredients have fresh status.",
                        "FRESH_OR_STALE_STATUS: the food can be crafted only when all ingredients are fresh or stale. If all food is fresh or stale, then AVERAGE logic will be applied, and when there are both, then WORST_STATUS will be applied."
                )
                .translation("config.notjustspoiled.food_crafting_mode")
                .worldRestart()
                .defineEnum("foodCraftingMode", FoodCraftingMode.FRESH_OR_STALE_STATUS);

        CONFIG = builder.build();
    }
}