package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.therealvoin.notjustspoiled.common.data.NJSTags;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;

public enum FoodCategory {
    RAW_FISH("raw_fish", NJSTags.Items.RAW_FISHES, NJSServerConfig.RAW_FISH_SPOILAGE_TIME),
    RAW_MEAT("raw_meat", NJSTags.Items.RAW_MEATS, NJSServerConfig.RAW_MEAT_SPOILAGE_TIME),
    RAW_VEGETABLE("raw_vegetable", NJSTags.Items.RAW_VEGETABLES, NJSServerConfig.RAW_VEGETABLE_SPOILAGE_TIME),
    COOKED_FISH("cooked_fish", NJSTags.Items.COOKED_FISHES, NJSServerConfig.COOKED_FISH_SPOILAGE_TIME),
    COOKED_MEAT("cooked_meat", NJSTags.Items.COOKED_MEATS, NJSServerConfig.COOKED_MEAT_SPOILAGE_TIME),
    COOKED_VEGETABLE("cooked_vegetable", NJSTags.Items.COOKED_VEGETABLES, NJSServerConfig.COOKED_VEGETABLE_SPOILAGE_TIME),
    BERRY("berry", NJSTags.Items.BERRIES, NJSServerConfig.BERRY_SPOILAGE_TIME),
    PASTRY("pastry", NJSTags.Items.PASTRY, NJSServerConfig.PASTRY_SPOILAGE_TIME),
    FRUIT("fruit", NJSTags.Items.FRUITS, NJSServerConfig.FRUIT_SPOILAGE_TIME),
    GRAIN("grain", NJSTags.Items.GRAINS, NJSServerConfig.GRAIN_SPOILAGE_TIME),
    RAW_EGG("raw_egg", NJSTags.Items.RAW_EGGS, NJSServerConfig.RAW_EGG_SPOILAGE_TIME),
    COOKED_EGG("cooked_egg", NJSTags.Items.COOKED_EGGS, NJSServerConfig.COOKED_EGG_SPOILAGE_TIME),
    MUSHROOM("mushroom", NJSTags.Items.MUSHROOMS, NJSServerConfig.MUSHROOM_SPOILAGE_TIME),
    STEW("stew", NJSTags.Items.STEWS, NJSServerConfig.STEW_SPOILAGE_TIME),
    SOUP("soup", NJSTags.Items.SOUPS, NJSServerConfig.SOUP_SPOILAGE_TIME),
    MILK("milk", NJSTags.Items.MILK, NJSServerConfig.MILK_SPOILAGE_TIME),
    RAW_INSECT("raw_insect", NJSTags.Items.RAW_INSECTS, NJSServerConfig.RAW_INSECT_SPOILAGE_TIME),
    COOKED_INSECT("cooked_insect", NJSTags.Items.COOKED_INSECTS, NJSServerConfig.COOKED_INSECT_SPOILAGE_TIME),
    SANDWICH("sandwich", NJSTags.Items.SANDWICHES, NJSServerConfig.SANDWICH_SPOILAGE_TIME),
    SALAD("salad", NJSTags.Items.SALADS, NJSServerConfig.SALAD_SPOILAGE_TIME),
    PORRIDGE("porridge", NJSTags.Items.PORRIDGES, NJSServerConfig.PORRIDGE_SPOILAGE_TIME),
    BREAD("bread", NJSTags.Items.BREADS, NJSServerConfig.BREAD_SPOILAGE_TIME),
    DRIED_FOOD("dried_food", NJSTags.Items.DRIED_FOODS, NJSServerConfig.DRIED_FOOD_SPOILAGE_TIME),
    SWEET("sweet", NJSTags.Items.SWEETS, NJSServerConfig.SWEET_SPOILAGE_TIME),
    DAIRY("dairy", NJSTags.Items.DAIRY, NJSServerConfig.DAIRY_SPOILAGE_TIME),
    PICKLED_FOOD("pickled_food", NJSTags.Items.PICKLED_FOODS, NJSServerConfig.PICKLED_FOOD_SPOILAGE_TIME),
    DISH("dish", NJSTags.Items.DISHES, NJSServerConfig.DISH_SPOILAGE_TIME),
    RAW_DOUGH("raw_dough", NJSTags.Items.RAW_DOUGH, NJSServerConfig.RAW_DOUGH_SPOILAGE_TIME),
    FOOD_DRESSING("food_dressing", NJSTags.Items.FOOD_DRESSING, NJSServerConfig.FOOD_DRESSING_SPOILAGE_TIME),
    RAW_SEAFOOD("raw_seafood", NJSTags.Items.RAW_SEAFOODS, NJSServerConfig.RAW_SEAFOOD_SPOILAGE_TIME),
    COOKED_SEAFOOD("cooked_seafood", NJSTags.Items.COOKED_SEAFOODS, NJSServerConfig.COOKED_SEAFOOD_SPOILAGE_TIME),
    NUT("nut", NJSTags.Items.NUTS, NJSServerConfig.NUT_SPOILAGE_TIME),
    DRY_PASTRY("dry_pastry", NJSTags.Items.DRY_PASTRY, NJSServerConfig.DRY_PASTRY_SPOILAGE_TIME),
    DRINK("drink", NJSTags.Items.DRINKS, NJSServerConfig.DRINK_SPOILAGE_TIME);

    private final String key;
    private final TagKey<Item> tag;
    private final ForgeConfigSpec.IntValue spoilageTime;

    FoodCategory(String key, TagKey<Item> tag, ForgeConfigSpec.IntValue spoilageTime) {
        this.key = key;
        this.tag = tag;
        this.spoilageTime = spoilageTime;
    }

    public Component getTranslation() {
        return Component.translatable("tooltip.notjustspoiled.food_category." + this.key).withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    public int getSpoilageTime() {
        return this.spoilageTime.get();
    }

    public static FoodCategory getFoodCategory(ItemStack stack) {
        for (FoodCategory foodCategory : values()) {
            if (stack.is(foodCategory.tag)) {
                return foodCategory;
            }
        }

        return null;
    }
}