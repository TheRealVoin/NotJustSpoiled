package net.therealvoin.notjustspoiled.core.foodspoilage;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.therealvoin.notjustspoiled.core.NJSTags;
import net.therealvoin.notjustspoiled.core.config.NJSServerConfig;

public enum FoodCategory {
    RAW_FISH(NJSServerConfig.RAW_FISH_SPOILAGE_TIME, NJSTags.Items.RAW_FISHES),
    RAW_MEAT(NJSServerConfig.RAW_MEAT_SPOILAGE_TIME, NJSTags.Items.RAW_MEATS),
    RAW_VEGETABLE(NJSServerConfig.RAW_VEGETABLE_SPOILAGE_TIME, NJSTags.Items.RAW_VEGETABLES),
    COOKED_FISH(NJSServerConfig.COOKED_FISH_SPOILAGE_TIME, NJSTags.Items.COOKED_FISHES),
    COOKED_MEAT(NJSServerConfig.COOKED_MEAT_SPOILAGE_TIME, NJSTags.Items.COOKED_MEATS),
    COOKED_VEGETABLE(NJSServerConfig.COOKED_VEGETABLE_SPOILAGE_TIME, NJSTags.Items.COOKED_VEGETABLES),
    BERRY(NJSServerConfig.BERRY_SPOILAGE_TIME, NJSTags.Items.BERRIES),
    PASTRY(NJSServerConfig.PASTRY_SPOILAGE_TIME, NJSTags.Items.PASTRY),
    FRUIT(NJSServerConfig.FRUIT_SPOILAGE_TIME, NJSTags.Items.FRUITS),
    GRAIN(NJSServerConfig.GRAIN_SPOILAGE_TIME, NJSTags.Items.GRAINS),
    RAW_EGG(NJSServerConfig.RAW_EGG_SPOILAGE_TIME, NJSTags.Items.RAW_EGGS),
    COOKED_EGG(NJSServerConfig.COOKED_EGG_SPOILAGE_TIME, NJSTags.Items.COOKED_EGGS),
    MUSHROOM(NJSServerConfig.MUSHROOM_SPOILAGE_TIME, NJSTags.Items.MUSHROOMS),
    STEW(NJSServerConfig.STEW_SPOILAGE_TIME, NJSTags.Items.STEWS),
    SOUP(NJSServerConfig.SOUP_SPOILAGE_TIME, NJSTags.Items.SOUPS),
    MILK(NJSServerConfig.MILK_SPOILAGE_TIME, NJSTags.Items.MILK),
    INSECT(NJSServerConfig.INSECT_SPOILAGE_TIME, NJSTags.Items.INSECTS),
    SANDWICH(NJSServerConfig.SANDWICH_SPOILAGE_TIME, NJSTags.Items.SANDWICHES),
    SALAD(NJSServerConfig.SALAD_SPOILAGE_TIME, NJSTags.Items.SALADS),
    PORRIDGE(NJSServerConfig.PORRIDGE_SPOILAGE_TIME, NJSTags.Items.PORRIDGES),
    BREAD(NJSServerConfig.BREAD_SPOILAGE_TIME, NJSTags.Items.BREADS),
    DRIED_FOOD(NJSServerConfig.DRIED_FOOD_SPOILAGE_TIME, NJSTags.Items.DRIED_FOODS),
    SWEET(NJSServerConfig.SWEET_SPOILAGE_TIME, NJSTags.Items.SWEETS),
    DAIRY(NJSServerConfig.DAIRY_SPOILAGE_TIME, NJSTags.Items.DAIRY),
    PICKLED_FOOD(NJSServerConfig.PICKLED_FOOD_SPOILAGE_TIME, NJSTags.Items.PICKLED_FOODS),
    COOKED_MEAL(NJSServerConfig.COOKED_MEAL_SPOILAGE_TIME, NJSTags.Items.COOKED_MEALS),
    RAW_DOUGH(NJSServerConfig.RAW_DOUGH_SPOILAGE_TIME, NJSTags.Items.RAW_DOUGH),
    FOOD_DRESSING(NJSServerConfig.FOOD_DRESSING_SPOILAGE_TIME, NJSTags.Items.FOOD_DRESSING);

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