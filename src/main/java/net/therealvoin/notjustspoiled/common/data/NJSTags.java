package net.therealvoin.notjustspoiled.common.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.therealvoin.notjustspoiled.NotJustSpoiled;

public class NJSTags {
    public static class Items {
        public static final TagKey<Item> ALWAYS_SPOILED = createTag("always_spoiled");
        public static final TagKey<Item> NEVER_SPOILS = createTag("never_spoils");

        public static final TagKey<Item> RAW_EGGS = createTag("raw_eggs");
        public static final TagKey<Item> RAW_FISHES = createTag("raw_fishes");
        public static final TagKey<Item> RAW_MEATS = createTag("raw_meats");
        public static final TagKey<Item> RAW_SEAFOODS = createTag("raw_seafoods");
        public static final TagKey<Item> RAW_INSECTS = createTag("raw_insects");
        public static final TagKey<Item> RAW_VEGETABLES = createTag("raw_vegetables");
        public static final TagKey<Item> RAW_DOUGH = createTag("raw_dough");

        public static final TagKey<Item> COOKED_EGGS = createTag("cooked_eggs");
        public static final TagKey<Item> COOKED_FISHES = createTag("cooked_fishes");
        public static final TagKey<Item> COOKED_MEATS = createTag("cooked_meats");
        public static final TagKey<Item> COOKED_SEAFOODS = createTag("cooked_seafoods");
        public static final TagKey<Item> COOKED_INSECTS = createTag("cooked_insects");
        public static final TagKey<Item> COOKED_VEGETABLES = createTag("cooked_vegetables");

        public static final TagKey<Item> BERRIES = createTag("berries");
        public static final TagKey<Item> FRUITS = createTag("fruits");
        public static final TagKey<Item> GRAINS = createTag("grains");
        public static final TagKey<Item> NUTS = createTag("nuts");
        public static final TagKey<Item> MUSHROOMS = createTag("mushrooms");

        public static final TagKey<Item> MILK = createTag("milk");
        public static final TagKey<Item> DAIRY = createTag("dairy");

        public static final TagKey<Item> BREADS = createTag("breads");
        public static final TagKey<Item> PASTRIES = createTag("pastries");
        public static final TagKey<Item> DRY_PASTRIES = createTag("dry_pastries");

        public static final TagKey<Item> STEWS = createTag("stews");
        public static final TagKey<Item> SOUPS = createTag("soups");
        public static final TagKey<Item> SANDWICHES = createTag("sandwiches");
        public static final TagKey<Item> SALADS = createTag("salads");
        public static final TagKey<Item> PORRIDGES = createTag("porridges");
        public static final TagKey<Item> DISHES = createTag("dishes");

        public static final TagKey<Item> SWEETS = createTag("sweets");
        public static final TagKey<Item> PICKLED_FOODS = createTag("pickled_foods");
        public static final TagKey<Item> FOOD_DRESSING = createTag("food_dressing");
        public static final TagKey<Item> DRIED_FOODS = createTag("dried_foods");
        public static final TagKey<Item> DRINKS = createTag("drinks");

        private static TagKey<Item> createTag(String tagName) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(NotJustSpoiled.MOD_ID, tagName));
        }
    }
}