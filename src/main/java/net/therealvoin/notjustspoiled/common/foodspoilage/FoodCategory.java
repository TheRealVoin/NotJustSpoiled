package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.data.NJSTags;
import net.therealvoin.notjustspoiled.common.data.foodcategory.FoodCategoryReloadListener;

public enum FoodCategory {
    RAW_FISH("raw_fish", NJSTags.Items.RAW_FISHES),
    RAW_MEAT("raw_meat", NJSTags.Items.RAW_MEATS),
    RAW_VEGETABLE("raw_vegetable", NJSTags.Items.RAW_VEGETABLES),
    COOKED_FISH("cooked_fish", NJSTags.Items.COOKED_FISHES),
    COOKED_MEAT("cooked_meat", NJSTags.Items.COOKED_MEATS),
    COOKED_VEGETABLE("cooked_vegetable", NJSTags.Items.COOKED_VEGETABLES),
    BERRY("berry", NJSTags.Items.BERRIES),
    PASTRY("pastry", NJSTags.Items.PASTRIES),
    FRUIT("fruit", NJSTags.Items.FRUITS),
    GRAIN("grain", NJSTags.Items.GRAINS),
    RAW_EGG("raw_egg", NJSTags.Items.RAW_EGGS),
    COOKED_EGG("cooked_egg", NJSTags.Items.COOKED_EGGS),
    MUSHROOM("mushroom", NJSTags.Items.MUSHROOMS),
    STEW("stew", NJSTags.Items.STEWS),
    SOUP("soup", NJSTags.Items.SOUPS),
    MILK("milk", NJSTags.Items.MILK),
    RAW_INSECT("raw_insect", NJSTags.Items.RAW_INSECTS),
    COOKED_INSECT("cooked_insect", NJSTags.Items.COOKED_INSECTS),
    SANDWICH("sandwich", NJSTags.Items.SANDWICHES),
    SALAD("salad", NJSTags.Items.SALADS),
    PORRIDGE("porridge", NJSTags.Items.PORRIDGES),
    BREAD("bread", NJSTags.Items.BREADS),
    DRIED_FOOD("dried_food", NJSTags.Items.DRIED_FOODS),
    SWEET("sweet", NJSTags.Items.SWEETS),
    DAIRY("dairy", NJSTags.Items.DAIRY),
    PICKLED_FOOD("pickled_food", NJSTags.Items.PICKLED_FOODS),
    DISH("dish", NJSTags.Items.DISHES),
    RAW_DOUGH("raw_dough", NJSTags.Items.RAW_DOUGH),
    FOOD_DRESSING("food_dressing", NJSTags.Items.FOOD_DRESSING),
    RAW_SEAFOOD("raw_seafood", NJSTags.Items.RAW_SEAFOODS),
    COOKED_SEAFOOD("cooked_seafood", NJSTags.Items.COOKED_SEAFOODS),
    NUT("nut", NJSTags.Items.NUTS),
    DRY_PASTRY("dry_pastry", NJSTags.Items.DRY_PASTRIES),
    DRINK("drink", NJSTags.Items.DRINKS);

    private final String key;
    private final TagKey<Item> tag;

    FoodCategory(String key, TagKey<Item> tag) {
        this.key = key;
        this.tag = tag;
    }

    public Component getDisplayName() {
        return Component.translatable("tooltip.notjustspoiled.food_category." + this.key).withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    public int getSpoilageTime() {
        return FoodCategoryReloadListener.get(this).spoilageTime();
    }

    public static FoodCategory byName(String name) {
        for (FoodCategory foodCategory : values()) {
            if (name.equals(foodCategory.key)) {
                return foodCategory;
            }
        }

        return null;
    }

    public static FoodCategory of(ItemStack stack) {
        for (FoodCategory foodCategory : values()) {
            if (stack.is(foodCategory.tag)) {
                return foodCategory;
            }
        }

        return null;
    }
}