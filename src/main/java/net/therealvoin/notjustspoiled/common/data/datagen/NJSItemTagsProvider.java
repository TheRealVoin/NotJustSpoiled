package net.therealvoin.notjustspoiled.common.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.common.data.NJSTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class NJSItemTagsProvider extends ItemTagsProvider {
    private IntrinsicTagAppender<Item> ALWAYS_SPOILED;
    private IntrinsicTagAppender<Item> NEVER_SPOILS;
    private IntrinsicTagAppender<Item> BERRIES;
    private IntrinsicTagAppender<Item> BREADS;
    private IntrinsicTagAppender<Item> COOKED_EGGS;
    private IntrinsicTagAppender<Item> COOKED_FISHES;
    private IntrinsicTagAppender<Item> COOKED_INSECTS;
    private IntrinsicTagAppender<Item> COOKED_MEATS;
    private IntrinsicTagAppender<Item> COOKED_SEAFOODS;
    private IntrinsicTagAppender<Item> COOKED_VEGETABLES;
    private IntrinsicTagAppender<Item> DAIRY;
    private IntrinsicTagAppender<Item> DISHES;
    private IntrinsicTagAppender<Item> DRIED_FOODS;
    private IntrinsicTagAppender<Item> DRINKS;
    private IntrinsicTagAppender<Item> DRY_PASTRY;
    private IntrinsicTagAppender<Item> FOOD_DRESSING;
    private IntrinsicTagAppender<Item> FRUITS;
    private IntrinsicTagAppender<Item> GRAINS;
    private IntrinsicTagAppender<Item> MILK;
    private IntrinsicTagAppender<Item> MUSHROOMS;
    private IntrinsicTagAppender<Item> NUTS;
    private IntrinsicTagAppender<Item> PASTRY;
    private IntrinsicTagAppender<Item> PICKLED_FOODS;
    private IntrinsicTagAppender<Item> PORRIDGES;
    private IntrinsicTagAppender<Item> RAW_DOUGH;
    private IntrinsicTagAppender<Item> RAW_EGGS;
    private IntrinsicTagAppender<Item> RAW_FISHES;
    private IntrinsicTagAppender<Item> RAW_INSECTS;
    private IntrinsicTagAppender<Item> RAW_MEATS;
    private IntrinsicTagAppender<Item> RAW_SEAFOODS;
    private IntrinsicTagAppender<Item> RAW_VEGETABLES;
    private IntrinsicTagAppender<Item> SALADS;
    private IntrinsicTagAppender<Item> SANDWICHES;
    private IntrinsicTagAppender<Item> SOUPS;
    private IntrinsicTagAppender<Item> STEWS;
    private IntrinsicTagAppender<Item> SWEETS;

    public NJSItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, NotJustSpoiled.MOD_ID, existingFileHelper);
    }

    private void init() {
        ALWAYS_SPOILED = tag(NJSTags.Items.ALWAYS_SPOILED);
        NEVER_SPOILS = tag(NJSTags.Items.NEVER_SPOILS);
        BERRIES = tag(NJSTags.Items.BERRIES);
        BREADS = tag(NJSTags.Items.BREADS);
        COOKED_EGGS = tag(NJSTags.Items.COOKED_EGGS);
        COOKED_FISHES = tag(NJSTags.Items.COOKED_FISHES);
        COOKED_INSECTS = tag(NJSTags.Items.COOKED_INSECTS);
        COOKED_MEATS = tag(NJSTags.Items.COOKED_MEATS);
        COOKED_SEAFOODS = tag(NJSTags.Items.COOKED_SEAFOODS);
        COOKED_VEGETABLES = tag(NJSTags.Items.COOKED_VEGETABLES);
        DAIRY = tag(NJSTags.Items.DAIRY);
        DISHES = tag(NJSTags.Items.DISHES);
        DRIED_FOODS = tag(NJSTags.Items.DRIED_FOODS);
        DRINKS = tag(NJSTags.Items.DRINKS);
        DRY_PASTRY = tag(NJSTags.Items.DRY_PASTRY);
        FOOD_DRESSING = tag(NJSTags.Items.FOOD_DRESSING);
        FRUITS = tag(NJSTags.Items.FRUITS);
        GRAINS = tag(NJSTags.Items.GRAINS);
        MILK = tag(NJSTags.Items.MILK);
        MUSHROOMS = tag(NJSTags.Items.MUSHROOMS);
        NUTS = tag(NJSTags.Items.NUTS);
        PASTRY = tag(NJSTags.Items.PASTRY);
        PICKLED_FOODS = tag(NJSTags.Items.PICKLED_FOODS);
        PORRIDGES = tag(NJSTags.Items.PORRIDGES);
        RAW_DOUGH = tag(NJSTags.Items.RAW_DOUGH);
        RAW_EGGS = tag(NJSTags.Items.RAW_EGGS);
        RAW_FISHES = tag(NJSTags.Items.RAW_FISHES);
        RAW_INSECTS = tag(NJSTags.Items.RAW_INSECTS);
        RAW_MEATS = tag(NJSTags.Items.RAW_MEATS);
        RAW_SEAFOODS = tag(NJSTags.Items.RAW_SEAFOODS);
        RAW_VEGETABLES = tag(NJSTags.Items.RAW_VEGETABLES);
        SALADS = tag(NJSTags.Items.SALADS);
        SANDWICHES = tag(NJSTags.Items.SANDWICHES);
        SOUPS = tag(NJSTags.Items.SOUPS);
        STEWS = tag(NJSTags.Items.STEWS);
        SWEETS = tag(NJSTags.Items.SWEETS);
    }

    private static void addOptional(IntrinsicTagAppender<Item> appender, String modId, String... names) {
        for (String name : names) {
            appender.addOptional(ResourceLocation.fromNamespaceAndPath(modId, name));
        }
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.init();

        minecraft();
        forge();
        alexsCaves("alexscaves");
        alexsMobs("alexsmobs");
        deeperAndDarker("deeperdarker");
        geneticAnimals("eanimod");
        endsDelight("ends_delight");
        farmersDelight("farmersdelight");
        oceansDelight("oceansdelight");
        simpleFarming("simplefarming");
        toughAsNails("toughasnails");
        yungsCaveBiomes("yungscavebiomes");
    }

    private void minecraft() {
        ALWAYS_SPOILED.add(
                Items.POISONOUS_POTATO,
                Items.PUFFERFISH,
                Items.ROTTEN_FLESH,
                Items.SPIDER_EYE
        );


        NEVER_SPOILS.add(
                Items.CHORUS_FRUIT,
                Items.COCOA_BEANS,
                Items.ENCHANTED_GOLDEN_APPLE,
                Items.GOLDEN_APPLE,
                Items.GOLDEN_CARROT,
                Items.HONEY_BLOCK,
                Items.HONEY_BOTTLE,
                Items.POTION,
                Items.SUGAR
        );

        BERRIES.add(
                Items.GLOW_BERRIES,
                Items.SWEET_BERRIES
        );

        BREADS.add(
                Items.BREAD
        );

        COOKED_FISHES.add(
                Items.COOKED_COD,
                Items.COOKED_SALMON
        );

        COOKED_MEATS.add(
                Items.COOKED_BEEF,
                Items.COOKED_CHICKEN,
                Items.COOKED_MUTTON,
                Items.COOKED_PORKCHOP,
                Items.COOKED_RABBIT
        );

        COOKED_VEGETABLES.add(
                Items.BAKED_POTATO
        );

        DRIED_FOODS.add(
                Items.DRIED_KELP
        );

        DRY_PASTRY.add(
                Items.COOKIE
        );

        FRUITS.add(
                Items.APPLE,
                Items.MELON,
                Items.MELON_SLICE
        );

        GRAINS.add(
                Items.HAY_BLOCK,
                Items.WHEAT
        );

        MILK.add(
                Items.MILK_BUCKET
        );

        MUSHROOMS.add(
                Items.BROWN_MUSHROOM,
                Items.CRIMSON_FUNGUS,
                Items.RED_MUSHROOM,
                Items.WARPED_FUNGUS
        );

        PASTRY.add(
                Items.CAKE,
                Items.PUMPKIN_PIE
        );

        RAW_FISHES.add(
                Items.COD,
                Items.SALMON,
                Items.TROPICAL_FISH
        );

        RAW_MEATS.add(
                Items.BEEF,
                Items.CHICKEN,
                Items.MUTTON,
                Items.PORKCHOP,
                Items.RABBIT
        );

        RAW_SEAFOODS.add(
                Items.KELP,
                Items.SEAGRASS
        );

        RAW_VEGETABLES.add(
                Items.BEETROOT,
                Items.CARROT,
                Items.POTATO,
                Items.PUMPKIN
        );

        SOUPS.add(
                Items.BEETROOT_SOUP
        );

        STEWS.add(
                Items.MUSHROOM_STEW,
                Items.RABBIT_STEW,
                Items.SUSPICIOUS_STEW
        );
    }

    private void forge() {
        RAW_EGGS.addTag(Tags.Items.EGGS);
    }

    private void alexsCaves(String modId) {
        addOptional(ALWAYS_SPOILED, modId,
                "stinky_fish"
        );

        addOptional(NEVER_SPOILS, modId,
                "peppermint_powder",
                "sprinkles"
        );

        addOptional(BERRIES, modId,
                "giant_sweetberry"
        );

        addOptional(COOKED_FISHES, modId,
                "cooked_lanternfish",
                "cooked_radgill",
                "cooked_tripodfish"
        );

        addOptional(COOKED_INSECTS, modId,
                "cooked_trilocaris_tail"
        );

        addOptional(COOKED_MEATS, modId,
                "cooked_dinosaur_chop",
                "dinosaur_nugget"
        );

        addOptional(COOKED_SEAFOODS, modId,
                "cooked_mussel"
        );

        addOptional(DAIRY, modId,
                "chocolate_ice_cream",
                "chocolate_ice_cream_scoop",
                "sundae",
                "sweetberry_ice_cream",
                "sweetberry_ice_cream_scoop",
                "vanilla_ice_cream",
                "vanilla_ice_cream_scoop"
        );

        addOptional(DISHES, modId,
                "alex_meal",
                "deep_sea_sushi_roll"
        );

        addOptional(DRINKS, modId,
                "hot_chocolate_bottle",
                "purple_soda_bottle"
        );

        addOptional(DRY_PASTRY, modId,
                "cake_layer",
                "cookie_block",
                "frosted_gingerbread_block",
                "frosted_gingerbread_brick_slab",
                "frosted_gingerbread_brick_stairs",
                "frosted_gingerbread_brick_wall",
                "frosted_gingerbread_bricks",
                "frosted_gingerbread_door",
                "frosted_gingerbread_slab",
                "frosted_gingerbread_stairs",
                "frosted_gingerbread_wall",
                "gingerbarrel",
                "gingerbread_block",
                "gingerbread_brick_slab",
                "gingerbread_brick_stairs",
                "gingerbread_brick_wall",
                "gingerbread_bricks",
                "gingerbread_crumbs",
                "gingerbread_door",
                "gingerbread_slab",
                "gingerbread_stairs",
                "gingerbread_wall",
                "wafer_cookie_block",
                "wafer_cookie_slab",
                "wafer_cookie_stairs",
                "wafer_cookie_wall"
        );

        addOptional(FRUITS, modId,
                "caramel_apple",
                "darkened_apple"
        );

        addOptional(NUTS, modId,
                "pine_nuts"
        );

        addOptional(PASTRY, modId,
                "dough_block"
        );

        addOptional(PICKLED_FOODS, modId,
                "slam"
        );

        addOptional(RAW_FISHES, modId,
                "lanternfish",
                "radgill",
                "tripodfish"
        );

        addOptional(RAW_INSECTS, modId,
                "trilocaris_tail"
        );

        addOptional(RAW_MEATS, modId,
                "dinosaur_chop",
                "vesper_wing"
        );

        addOptional(RAW_SEAFOODS, modId,
                "mussel",
                "sea_pig"
        );

        addOptional(RAW_VEGETABLES, modId,
                "green_soylent"
        );

        addOptional(SALADS, modId,
                "serene_salad"
        );

        addOptional(SOUPS, modId,
                "primordial_soup"
        );

        addOptional(STEWS, modId,
                "seething_stew",
                "vesper_stew"
        );

        addOptional(SWEETS, modId,
                "biome_treat",
                "block_of_chiseled_chocolate",
                "block_of_chocolate",
                "block_of_chocolate_frosting",
                "block_of_frosted_chocolate",
                "block_of_frosting",
                "block_of_polished_chocolate",
                "block_of_vanilla_frosting",
                "candy_cane",
                "candy_cane_block",
                "candy_cane_pole",
                "caramel",
                "chiseled_candy_cane_block",
                "frostmint",
                "gelatin_blue",
                "gelatin_green",
                "gelatin_pink",
                "gelatin_red",
                "gelatin_yellow",
                "gumball_pile",
                "gummy_ring_blue",
                "gummy_ring_green",
                "gummy_ring_pink",
                "gummy_ring_red",
                "gummy_ring_yellow",
                "jelly_bean",
                "large_peppermint",
                "licoroot",
                "licoroot_sprout",
                "licoroot_vine",
                "lollipop_bunch",
                "rock_candy_black",
                "rock_candy_blue",
                "rock_candy_brown",
                "rock_candy_cyan",
                "rock_candy_gray",
                "rock_candy_green",
                "rock_candy_light_blue",
                "rock_candy_light_gray",
                "rock_candy_lime",
                "rock_candy_magenta",
                "rock_candy_orange",
                "rock_candy_pink",
                "rock_candy_purple",
                "rock_candy_red",
                "rock_candy_white",
                "rock_candy_yellow",
                "sharpened_candy_cane",
                "small_peppermint",
                "spelunkie",
                "stripped_candy_cane_block",
                "stripped_candy_cane_pole",
                "sugar_glass",
                "sundrop",
                "sweet_puff",
                "sweetish_fish_blue",
                "sweetish_fish_green",
                "sweetish_fish_pink",
                "sweetish_fish_red",
                "sweetish_fish_yellow"
        );
    }

    private void alexsMobs(String modId) {
        addOptional(NEVER_SPOILS, modId,
                "fish_oil",
                "rainbow_jelly"
        );

        addOptional(COOKED_EGGS, modId,
                "boiled_emu_egg"
        );

        addOptional(COOKED_FISHES, modId,
                "cooked_catfish"
        );

        addOptional(COOKED_MEATS, modId,
                "cooked_kangaroo_meat",
                "cooked_moose_ribs"
        );

        addOptional(COOKED_SEAFOODS, modId,
                "cooked_lobster_tail"
        );

        addOptional(DISHES, modId,
                "shrimp_fried_rice"
        );

        addOptional(FRUITS, modId,
                "banana"
        );

        addOptional(MUSHROOMS, modId,
                "gongylidia"
        );

        addOptional(RAW_FISHES, modId,
                "blobfish",
                "cosmic_cod",
                "flying_fish",
                "raw_catfish"
        );

        addOptional(RAW_INSECTS, modId,
                "maggot"
        );

        addOptional(RAW_MEATS, modId,
                "kangaroo_meat",
                "moose_ribs"
        );

        addOptional(RAW_SEAFOODS, modId,
                "lobster_tail"
        );

        addOptional(SANDWICHES, modId,
                "kangaroo_burger"
        );

        addOptional(SOUPS, modId,
                "sopa_de_macaco"
        );

        addOptional(STEWS, modId,
                "mosquito_repellent_stew"
        );
    }

    private void deeperAndDarker(String modId) {
        addOptional(BERRIES, modId,
                "bloom_berries"
        );
    }

    private void geneticAnimals(String modId) {
        addOptional(COOKED_MEATS, modId,
                "cookedchicken_darkbig",
                "cookedchicken_darksmall",
                "cookedchicken_dark",
                "cookedchicken_palesmall",
                "cookedchicken_pale",
                "cookedrabbit_small"
        );

        addOptional(GRAINS, modId,
                "unboundhay_block"
        );

        addOptional(MILK, modId,
                "fivesixths_milk_bucket",
                "half_milk_bottle",
                "half_milk_bucket",
                "milk_bottle",
                "onesixth_milk_bucket",
                "onethird_milk_bucket",
                "twothirds_milk_bucket"
        );

        addOptional(RAW_MEATS, modId,
                "rawchicken_darkbig",
                "rawchicken_darksmall",
                "rawchicken_dark",
                "rawchicken_palesmall",
                "rawchicken_pale",
                "rawrabbit_small"
        );

        addOptional(STEWS, modId,
                "rabbitstew_weak"
        );
    }

    private void endsDelight(String modId) {
        addOptional(NEVER_SPOILS, modId,
                "chorus_fruit_crate",
                "chorus_fruit_grain",
                "chorus_fruit_wine",
                "dragon_breath_soda"
        );

        addOptional(COOKED_EGGS, modId,
                "fried_dragon_egg"
        );

        addOptional(COOKED_MEATS, modId,
                "ender_sausage",
                "roasted_dragon_meat",
                "roasted_dragon_meat_cuts",
                "roasted_shulker_meat",
                "roasted_shulker_meat_slice",
                "smoked_dragon_leg"
        );

        addOptional(DISHES, modId,
                "dragon_leg_with_sauce",
                "dragon_leg_with_sauce_block",
                "end_barbecue_stick",
                "ender_bamboo_rice",
                "ender_noodle",
                "grilled_shulker",
                "grilled_shulker_block",
                "roasted_dragon_steak",
                "shulker_omelette",
                "shulker_omelette_mixture",
                "steamed_dragon_egg",
                "steamed_dragon_egg_block",
                "stir_fried_shulker_meat"
        );

        addOptional(DRIED_FOODS, modId,
                "dried_endermite_meat"
        );

        addOptional(DRINKS, modId,
                "bubble_tea",
                "chorus_flower_tea",
                "chorus_fruit_milk_tea"
        );

        addOptional(DRY_PASTRY, modId,
                "chorus_cookie"
        );

        addOptional(FOOD_DRESSING, modId,
                "chorus_sauce"
        );

        addOptional(FRUITS, modId,
                "chorus_succulent"
        );

        addOptional(PASTRY, modId,
                "chorus_flower_pie",
                "chorus_fruit_pie",
                "chorus_fruit_pie_slice",
                "stuffed_rice_cake"
        );

        addOptional(PORRIDGES, modId,
                "ender_congee"
        );

        addOptional(RAW_EGGS, modId,
                "liquid_dragon_egg"
        );

        addOptional(RAW_MEATS, modId,
                "dragon_leg",
                "enderman_gristle",
                "raw_dragon_meat",
                "raw_dragon_meat_cuts",
                "raw_ender_mite_meat",
                "raw_ender_sausage",
                "shulker_meat",
                "shulker_meat_slice"
        );

        addOptional(SALADS, modId,
                "assorted_salad",
                "end_mixed_salad"
        );

        addOptional(SOUPS, modId,
                "dragon_breath_and_chorus_soup",
                "shulker_soup"
        );

        addOptional(STEWS, modId,
                "dragon_meat_stew",
                "dragon_meat_stew_block",
                "enderman_gristle_stew"
        );

        addOptional(SWEETS, modId,
                "chorus_fruit_popsicle"
        );
    }

    private void farmersDelight(String modId) {
        addOptional(ALWAYS_SPOILED, modId,
                "rotten_tomato"
        );

        addOptional(COOKED_EGGS, modId,
                "fried_egg"
        );

        addOptional(COOKED_FISHES, modId,
                "cooked_cod_slice",
                "cooked_salmon_slice"
        );

        addOptional(COOKED_MEATS, modId,
                "beef_patty",
                "cooked_bacon",
                "cooked_chicken_cuts",
                "cooked_mutton_chops",
                "smoked_ham"
        );

        addOptional(DAIRY, modId,
                "glow_berry_custard"
        );

        addOptional(DISHES, modId,
                "bacon_and_eggs",
                "barbecue_stick",
                "cabbage_rolls",
                "cod_roll",
                "dog_food",
                "dumplings",
                "fried_rice",
                "grilled_salmon",
                "honey_glazed_ham",
                "honey_glazed_ham_block",
                "kelp_roll",
                "kelp_roll_slice",
                "mushroom_rice",
                "mutton_wrap",
                "pasta_with_meatballs",
                "pasta_with_mutton_chop",
                "ratatouille",
                "rice_roll_medley_block",
                "roasted_mutton_chops",
                "roast_chicken",
                "roast_chicken_block",
                "salmon_roll",
                "shepherds_pie",
                "shepherds_pie_block",
                "squid_ink_pasta",
                "steak_and_potatoes",
                "stuffed_potato",
                "stuffed_pumpkin",
                "stuffed_pumpkin_block",
                "vegetable_noodles"
        );

        addOptional(DRINKS, modId,
                "apple_cider",
                "hot_cocoa",
                "melon_juice"
        );

        addOptional(DRY_PASTRY, modId,
                "honey_cookie",
                "sweet_berry_cookie"
        );

        addOptional(FOOD_DRESSING, modId,
                "tomato_sauce"
        );

        addOptional(GRAINS, modId,
                "horse_feed",
                "rice",
                "rice_bag",
                "rice_bale",
                "rice_panicle"
        );

        addOptional(MILK, modId,
                "milk_bottle"
        );

        addOptional(MUSHROOMS, modId,
                "brown_mushroom_colony",
                "red_mushroom_colony"
        );

        addOptional(PASTRY, modId,
                "apple_pie",
                "apple_pie_slice",
                "cake_slice",
                "chocolate_pie",
                "chocolate_pie_slice",
                "pie_crust",
                "pumpkin_pie_slice",
                "sweet_berry_cheesecake",
                "sweet_berry_cheesecake_slice"
        );

        addOptional(PORRIDGES, modId,
                "cooked_rice"
        );

        addOptional(RAW_DOUGH, modId,
                "wheat_dough",
                "raw_pasta"
        );

        addOptional(RAW_FISHES, modId,
                "cod_slice",
                "salmon_slice"
        );

        addOptional(RAW_MEATS, modId,
                "bacon",
                "chicken_cuts",
                "ham",
                "minced_beef",
                "mutton_chops"
        );

        addOptional(RAW_VEGETABLES, modId,
                "beetroot_crate",
                "cabbage",
                "cabbage_crate",
                "cabbage_leaf",
                "carrot_crate",
                "onion",
                "onion_crate",
                "potato_crate",
                "pumpkin_slice",
                "tomato",
                "tomato_crate"
        );

        addOptional(SALADS, modId,
                "fruit_salad",
                "gleaming_salad",
                "gleaming_salad_block",
                "mixed_salad",
                "nether_salad"
        );

        addOptional(SANDWICHES, modId,
                "bacon_sandwich",
                "chicken_sandwich",
                "egg_sandwich",
                "hamburger"
        );

        addOptional(SOUPS, modId,
                "bone_broth",
                "chicken_soup",
                "noodle_soup",
                "onion_soup",
                "pumpkin_soup",
                "vegetable_soup"
        );

        addOptional(STEWS, modId,
                "baked_cod_stew",
                "beef_stew",
                "fish_stew"
        );

        addOptional(SWEETS, modId,
                "melon_popsicle"
        );
    }

    private void oceansDelight(String modId) {
        addOptional(COOKED_SEAFOODS, modId,
                "baked_tentacle_on_a_stick",
                "cooked_elder_guardian_slice",
                "cooked_guardian_tail"
        );

        addOptional(DISHES, modId,
                "cabbage_wrapped_elder_guardian",
                "cooked_stuffed_cod",
                "elder_guardian_roll",
                "fugu_roll",
                "honey_fried_kelp",
                "squid_rings",
                "stuffed_cod"
        );

        addOptional(RAW_FISHES, modId,
                "fugu_slice"
        );

        addOptional(RAW_SEAFOODS, modId,
                "cut_tentacles",
                "elder_guardian_slab",
                "elder_guardian_slice",
                "guardian",
                "guardian_tail",
                "tentacle_on_a_stick",
                "tentacles"
        );

        addOptional(SALADS, modId,
                "seagrass_salad"
        );

        addOptional(SOUPS, modId,
                "bowl_of_guardian_soup",
                "guardian_soup"
        );

        addOptional(STEWS, modId,
                "braised_sea_pickle"
        );
    }

    private void simpleFarming(String modId) {
        addOptional(NEVER_SPOILS, modId,
                "beer",
                "cauim",
                "cider",
                "curry_powder",
                "golden_habanero",
                "mead",
                "olive_oil",
                "sake",
                "sprinkles",
                "tiswin",
                "vinegar",
                "vodka",
                "whiskey",
                "wine"
        );

        addOptional(BERRIES, modId,
                "blackberries",
                "blueberries",
                "raspberries",
                "strawberries"
        );

        addOptional(BREADS, modId,
                "banana_bread",
                "barley_bread",
                "cornbread",
                "oat_bread",
                "rice_bread",
                "rye_bread",
                "sorghum_bread",
                "zucchini_bread"
        );

        addOptional(COOKED_EGGS, modId,
                "cooked_egg"
        );

        addOptional(COOKED_MEATS, modId,
                "cooked_bacon",
                "cooked_chicken_wings",
                "cooked_sausage"
        );

        addOptional(COOKED_VEGETABLES, modId,
                "baked_sweet_potato",
                "baked_yam"
        );

        addOptional(DAIRY, modId,
                "cheese_slice",
                "cheese_wheel",
                "ice_cream_sundae",
                "tofu"
        );

        addOptional(DISHES, modId,
                "beef_and_broccoli",
                "beef_curry",
                "chicken_curry",
                "chicken_parmesan",
                "chicory_gratin",
                "eggplant_parmesan",
                "fish_and_chips",
                "fish_fillet",
                "fried_rice",
                "lasagna",
                "mac_and_cheese",
                "mutton_curry",
                "mushroom_barley",
                "pad_thai",
                "pasta",
                "pizza",
                "pork_curry",
                "potato_knish",
                "spaghetti",
                "spinach_mushroom_quiche",
                "spinach_quinoa_quiche",
                "stuffed_corn_zucchini",
                "sushi",
                "squash_casserole",
                "tofu_scramble",
                "turnip_beetroot_gratin",
                "vegetable_curry"
        );

        addOptional(DRIED_FOODS, modId,
                "popcorn",
                "raisins",
                "trail_mix"
        );

        addOptional(DRY_PASTRY, modId,
                "peanut_butter_cookie"
        );

        addOptional(FRUITS, modId,
                "apricot",
                "banana",
                "cactus_fruit",
                "cherries",
                "grapes",
                "mango",
                "olives",
                "orange",
                "pear",
                "plum"
        );

        addOptional(GRAINS, modId,
                "barley",
                "barley_hay_block",
                "corn",
                "cumin_seeds",
                "oat",
                "oat_hay_block",
                "quinoa_seeds",
                "rice",
                "rice_hay_block",
                "rye",
                "rye_hay_block",
                "sorghum"
        );

        addOptional(NUTS, modId,
                "peanut"
        );

        addOptional(PASTRY, modId,
                "apple_pie",
                "apricot_pie",
                "blackberry_pie",
                "blueberry_pie",
                "cassava_cake",
                "cherry_pie",
                "chocolate_cake",
                "jaffa_cake",
                "pancakes",
                "peanut_butter_pie",
                "pear_pie",
                "plum_pie",
                "raspberry_pie",
                "sprinkle_cake",
                "strawberry_pie",
                "sweet_potato_quinoa_cakes"
        );

        addOptional(PICKLED_FOODS, modId,
                "pickle",
                "pickled_beetroot"
        );

        addOptional(PORRIDGES, modId,
                "oatmeal",
                "rice_bowl",
                "sorghum_porridge"
        );

        addOptional(RAW_DOUGH, modId,
                "noodles"
        );

        addOptional(RAW_MEATS, modId,
                "raw_bacon",
                "raw_chicken_wings",
                "raw_sausage"
        );

        addOptional(RAW_VEGETABLES, modId,
                "broccoli",
                "cantaloupe",
                "cantaloupe_block",
                "cassava",
                "chicory_root",
                "cucumber",
                "eggplant",
                "ginger",
                "habanero",
                "honeydew",
                "honeydew_block",
                "lettuce",
                "marshmallow_root",
                "onion",
                "pea_pod",
                "pepper",
                "radish",
                "soybean",
                "spinach",
                "squash",
                "squash_block",
                "sweet_potato",
                "tomato",
                "turnip",
                "vegetable_medley",
                "yam",
                "zucchini"
        );

        addOptional(SALADS, modId,
                "caesar_salad",
                "corn_salad",
                "fruit_salad",
                "olive_tomato_salad",
                "quinoa_salad",
                "salad"
        );

        addOptional(SANDWICHES, modId,
                "blt",
                "cheeseburger",
                "egg_sandwich",
                "fish_sandwich",
                "hamburger",
                "hotdog",
                "italian_beef",
                "pbj",
                "pulled_pork_sandwich",
                "sandwich",
                "veggie_burger"
        );

        addOptional(SOUPS, modId,
                "borscht",
                "broccoli_cheese_soup",
                "carrot_soup",
                "chicken_noodle_soup",
                "cucumber_soup",
                "onion_soup",
                "pea_soup",
                "pumpkin_soup",
                "radish_soup",
                "sausage_barley_soup",
                "squash_soup",
                "tomato_soup"
        );

        addOptional(STEWS, modId,
                "chili"
        );

        addOptional(SWEETS, modId,
                "candy",
                "candy_cane",
                "chocolate",
                "jam",
                "marshmallow"
        );
    }

    private void toughAsNails(String modId) {
        addOptional(NEVER_SPOILS, modId,
                "charc_os",
                "copper_dirty_water_canteen",
                "copper_purified_water_canteen",
                "copper_water_canteen",
                "diamond_dirty_water_canteen",
                "diamond_purified_water_canteen",
                "diamond_water_canteen",
                "dirty_water_bottle",
                "gold_dirty_water_canteen",
                "gold_purified_water_canteen",
                "gold_water_canteen",
                "ice_cream",
                "iron_dirty_water_canteen",
                "iron_purified_water_canteen",
                "iron_water_canteen",
                "leather_dirty_water_canteen",
                "leather_purified_water_canteen",
                "leather_water_canteen",
                "netherite_dirty_water_canteen",
                "netherite_purified_water_canteen",
                "netherite_water_canteen",
                "purified_water_bottle"
        );

        addOptional(DRINKS, modId,
                "apple_juice",
                "cactus_juice",
                "chorus_fruit_juice",
                "glow_berry_juice",
                "melon_juice",
                "pumpkin_juice",
                "sweet_berry_juice"
        );
    }

    private void yungsCaveBiomes(String modId) {
        addOptional(FRUITS, modId,
                "prickly_peach"
        );
    }
}