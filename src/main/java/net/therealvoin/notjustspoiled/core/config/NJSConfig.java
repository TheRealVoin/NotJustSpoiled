package net.therealvoin.notjustspoiled.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NJSConfig {
    public static final ForgeConfigSpec CONFIG;
    public static final ForgeConfigSpec.Builder BUILDER;

    public static final ForgeConfigSpec.IntValue RAW_FISH_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue RAW_MEAT_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue RAW_VEGETABLE_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue COOKED_FISH_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue COOKED_MEAT_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue COOKED_VEGETABLE_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue PASTRY_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue BERRY_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue FRUIT_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue GRAIN_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue RAW_EGG_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue COOKED_EGG_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue MUSHROOM_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue STEW_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue SOUP_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue MILK_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue INSECT_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue SANDWICH_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue SALAD_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue PORRIDGE_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue BREAD_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue DRIED_FOOD_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue SWEET_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue DAIRY_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue PICKLED_FOOD_SPOILAGE_TIME;
    public static final ForgeConfigSpec.IntValue COOKED_MEAL_SPOILAGE_TIME;

    public static final ForgeConfigSpec.DoubleValue FOOD_SPOILAGE_IN_INVENTORY_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FOOD_SPOILAGE_IN_STORAGE_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FOOD_SPOILAGE_ON_GROUND_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FOOD_SPOILAGE_WHILE_COOKING_MULTIPLIER;

    public static final ForgeConfigSpec.DoubleValue CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE;
    public static final ForgeConfigSpec.DoubleValue CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE;
    public static final ForgeConfigSpec.DoubleValue CHANCE_TO_APPEAR_HALF_SPOILED_FOOD_IN_STORAGE;
    public static final ForgeConfigSpec.DoubleValue CHANCE_TO_APPEAR_SPOILED_FOOD_IN_STORAGE;

    public static final ForgeConfigSpec.EnumValue<CraftingMode> CRAFTING_MODE;

    private static final int MIN_FOOD_SPOILAGE_TIME = 0;
    private static final int MAX_FOOD_SPOILAGE_TIME = Integer.MAX_VALUE;

    static {
        BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.translation("config.notjustspoiled.category.food_spoilage_time").push("Food Spoilage Time");
        RAW_FISH_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.raw_fish_spoilage_time")
                .defineInRange("rawFishSpoilageTime", 24000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        RAW_MEAT_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.raw_meat_spoilage_time")
                .defineInRange("rawMeatSpoilageTime", 36000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        RAW_VEGETABLE_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.raw_vegetable_spoilage_time")
                .defineInRange("rawVegetableSpoilageTime", 744000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        COOKED_FISH_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.cooked_fish_spoilage_time")
                .defineInRange("cookedFishSpoilageTime", 36000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        COOKED_MEAT_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.cooked_meat_spoilage_time")
                .defineInRange("cookedMeatSpoilageTime", 60000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        COOKED_VEGETABLE_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.cooked_vegetable_spoilage_time")
                .defineInRange("cookedVegetableSpoilageTime", 96000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        PASTRY_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.pastry_spoilage_time")
                .defineInRange("pastrySpoilageTime", 96000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        BERRY_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.berry_spoilage_time")
                .defineInRange("berrySpoilageTime", 60000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        FRUIT_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.fruit_spoilage_time")
                .defineInRange("fruitSpoilageTime", 504000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        GRAIN_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.grain_spoilage_time")
                .defineInRange("grainSpoilageTime", 5208000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        RAW_EGG_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.raw_egg_spoilage_time")
                .defineInRange("eggSpoilageTime", 168000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        COOKED_EGG_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.cooked_egg_spoilage_time")
                .defineInRange("cookedEggSpoilageTime", 120000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        MUSHROOM_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.mushroom_spoilage_time")
                .defineInRange("mushroomSpoilageTime", 36000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        STEW_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.stew_spoilage_time")
                .defineInRange("stewSpoilageTime", 96000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        SOUP_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.soup_spoilage_time")
                .defineInRange("soupSpoilageTime", 84000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        MILK_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.milk_spoilage_time")
                .defineInRange("milkSpoilageTime", 36000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        INSECT_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.insect_spoilage_time")
                .defineInRange("insectSpoilageTime", 60000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        SANDWICH_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.sandwich_spoilage_time")
                .defineInRange("sandwichSpoilageTime", 84000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        SALAD_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.salad_spoilage_time")
                .defineInRange("saladSpoilageTime", 48000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        PORRIDGE_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.porridge_spoilage_time")
                .defineInRange("porridgeSpoilageTime", 120000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        BREAD_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.bread_spoilage_time")
                .defineInRange("breadSpoilageTime", 72000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        DRIED_FOOD_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.dried_food_spoilage_time")
                .defineInRange("driedFoodSpoilageTime", 4464000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        SWEET_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.sweet_spoilage_time")
                .defineInRange("sweetSpoilageTime", 2232000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        DAIRY_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.dairy_spoilage_time")
                .defineInRange("dairySpoilageTime", 72000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        PICKLED_FOOD_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.pickled_food_spoilage_time")
                .defineInRange("pickledFoodSpoilageTime", 6696000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);

        COOKED_MEAL_SPOILAGE_TIME = BUILDER
                .translation("config.notjustspoiled.cooked_meal_spoilage_time")
                .defineInRange("cookedMealSpoilageTime", 120000, MIN_FOOD_SPOILAGE_TIME, MAX_FOOD_SPOILAGE_TIME);
        BUILDER.pop();


        BUILDER.translation("config.notjustspoiled.category.food_spoilage_multiplier").push("Food Spoilage Multiplier");
        FOOD_SPOILAGE_IN_INVENTORY_MULTIPLIER = BUILDER
                .comment("A modifier that affects the speed of food spoiling in inventory")
                .defineInRange("foodSpoilageInInventoryMultiplier", 1, 0.1, 10);

        FOOD_SPOILAGE_IN_STORAGE_MULTIPLIER = BUILDER
                .comment("A modifier that affects the speed of food spoiling in storage")
                .defineInRange("foodSpoilageInStorageMultiplier", 0.7, 0.1, 10);

        FOOD_SPOILAGE_ON_GROUND_MULTIPLIER = BUILDER
                .comment("A modifier that affects the speed of food spoiling on ground")
                .defineInRange("foodSpoilageOnGroundMultiplier", 2, 0.1, 10);

        FOOD_SPOILAGE_WHILE_COOKING_MULTIPLIER = BUILDER
                .comment("A modifier that affects the speed of food spoiling while cooking")
                .defineInRange("foodSpoilageWhileCookingMultiplier", 0.1, 0.1, 10);
        BUILDER.pop();

        BUILDER.translation("config.notjustspoiled.category.chance_to_appear_in_storage").push("Chance to appear in storage");
        CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE = BUILDER
                .comment("Chance of fresh food to appear in storage (Note: all 4 values must add up to 1.0)")
                .defineInRange("chanceToAppearFreshFoodInStorage", 0.1, 0, 1.0);
        CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE = BUILDER
                .comment("Chance of stale food to appear in storage (Note: all 4 values must add up to 1.0)")
                .defineInRange("chanceToAppearStaleFoodInStorage", 0.2, 0, 1.0);
        CHANCE_TO_APPEAR_HALF_SPOILED_FOOD_IN_STORAGE = BUILDER
                .comment("Chance of half-spoiled food to appear in storage (Note: all 4 values must add up to 1.0)")
                .defineInRange("chanceToAppearHalfSpoiledFoodInStorage", 0.3, 0, 1.0);
        CHANCE_TO_APPEAR_SPOILED_FOOD_IN_STORAGE = BUILDER
                .comment("Chance of spoiled food to appear in storage (Note: all 4 values must add up to 1.0)")
                .defineInRange("chanceToAppearSpoiledFoodInStorage", 0.4, 0, 1.0);
        BUILDER.pop();

        BUILDER.translation("config.notjustspoiled.category.crafting_mode").push("Crafting Mode");
        CRAFTING_MODE = BUILDER
                .defineEnum("craftingMode", CraftingMode.AVERAGE);
        BUILDER.pop();

        CONFIG = BUILDER.build();
    }
}