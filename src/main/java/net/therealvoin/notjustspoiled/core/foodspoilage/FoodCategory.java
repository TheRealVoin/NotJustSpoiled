package net.therealvoin.notjustspoiled.core.foodspoilage;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.therealvoin.notjustspoiled.core.NJSTags;
import net.therealvoin.notjustspoiled.core.config.NJSConfig;

public enum FoodCategory {
    RAW_FISH(NJSConfig.RAW_FISH_SPOILAGE_TIME, NJSTags.Items.RAW_FISHES),
    RAW_MEAT(NJSConfig.RAW_MEAT_SPOILAGE_TIME, NJSTags.Items.RAW_MEATS),
    RAW_VEGETABLE(NJSConfig.RAW_VEGETABLE_SPOILAGE_TIME, NJSTags.Items.RAW_VEGETABLES),
    COOKED_FISH(NJSConfig.COOKED_FISH_SPOILAGE_TIME, NJSTags.Items.COOKED_FISHES),
    COOKED_MEAT(NJSConfig.COOKED_MEAT_SPOILAGE_TIME, NJSTags.Items.COOKED_MEATS),
    COOKED_VEGETABLE(NJSConfig.COOKED_VEGETABLE_SPOILAGE_TIME, NJSTags.Items.COOKED_VEGETABLES),
    BERRY(NJSConfig.BERRY_SPOILAGE_TIME, NJSTags.Items.BERRIES),
    PASTRY(NJSConfig.PASTRY_SPOILAGE_TIME, NJSTags.Items.PASTRY),
    FRUIT(NJSConfig.FRUIT_SPOILAGE_TIME, NJSTags.Items.FRUITS),
    GRAIN(NJSConfig.GRAIN_SPOILAGE_TIME, NJSTags.Items.GRAINS),
    RAW_EGG(NJSConfig.RAW_EGG_SPOILAGE_TIME, NJSTags.Items.RAW_EGGS),
    COOKED_EGG(NJSConfig.COOKED_EGG_SPOILAGE_TIME, NJSTags.Items.COOKED_EGGS),
    MUSHROOM(NJSConfig.MUSHROOM_SPOILAGE_TIME, NJSTags.Items.MUSHROOMS),
    STEW(NJSConfig.STEW_SPOILAGE_TIME, NJSTags.Items.STEWS),
    SOUP(NJSConfig.SOUP_SPOILAGE_TIME, NJSTags.Items.SOUPS),
    MILK(NJSConfig.MILK_SPOILAGE_TIME, NJSTags.Items.MILK),
    INSECT(NJSConfig.INSECT_SPOILAGE_TIME, NJSTags.Items.INSECTS),
    SANDWICH(NJSConfig.SANDWICH_SPOILAGE_TIME, NJSTags.Items.SANDWICHES),
    SALAD(NJSConfig.SALAD_SPOILAGE_TIME, NJSTags.Items.SALADS),
    PORRIDGE(NJSConfig.PORRIDGE_SPOILAGE_TIME, NJSTags.Items.PORRIDGES),
    BREAD(NJSConfig.BREAD_SPOILAGE_TIME, NJSTags.Items.BREADS),
    DRIED_FOOD(NJSConfig.DRIED_FOOD_SPOILAGE_TIME, NJSTags.Items.DRIED_FOODS),
    SWEET(NJSConfig.SWEET_SPOILAGE_TIME, NJSTags.Items.SWEETS),
    DAIRY(NJSConfig.DAIRY_SPOILAGE_TIME, NJSTags.Items.DAIRY),
    PICKLED_FOOD(NJSConfig.PICKLED_FOOD_SPOILAGE_TIME, NJSTags.Items.PICKLED_FOODS),
    COOKED_MEAL(NJSConfig.COOKED_MEAL_SPOILAGE_TIME, NJSTags.Items.COOKED_MEALS);

    private final ForgeConfigSpec.IntValue spoilageTime;
    private final TagKey<Item> tagKey;

    FoodCategory(ForgeConfigSpec.IntValue spoilageTime, TagKey<Item> tagKey) {
        this.spoilageTime = spoilageTime;
        this.tagKey = tagKey;
    }

    public int getSpoilageTime() {
        return this.spoilageTime.get();
    }

    public static FoodCategory getFoodCategory(ItemStack itemStack) {
        for (FoodCategory foodCategory : values()) {
            if (itemStack.is(foodCategory.tagKey)) {
                return foodCategory;
            }
        }

        return null;
    }
}