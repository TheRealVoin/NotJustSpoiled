package net.therealvoin.notjustspoiled.client.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NJSClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec.Builder BUILDER;

    public static final ForgeConfigSpec.BooleanValue SHOW_CATEGORY_IN_TOOLTIP;
    public static final ForgeConfigSpec.BooleanValue SHOW_ADDITIONAL_INFO;
    public static final ForgeConfigSpec.BooleanValue FOOD_OVERLAY;
    public static final ForgeConfigSpec.BooleanValue SHOW_REMAINING_DAYS;
    public static final ForgeConfigSpec.BooleanValue SHOW_REMAINING_DAYS_TO_BEING_SPOILED;

    static {
        BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.translation("config.notjustspoiled.category.debug").push("Debug");
        SHOW_ADDITIONAL_INFO = BUILDER
                .comment("Shows additional food info in tooltip (\"lastUpdateTime\", \"foodLifeTime\" and \"environment\").")
                .translation("config.notjustspoiled.debug.show_additional_info")
                .define("showAdditionalInfo", false);
        BUILDER.pop();

        BUILDER.translation("config.notjustspoiled.category.tooltip").push("Tooltip");
        SHOW_CATEGORY_IN_TOOLTIP = BUILDER
                .comment("Shows food category in tooltip. If food doesn't have category, then nothing will be displayed.")
                .translation("config.notjustspoiled.tooltip.show_food_category")
                .define("showCategoryInTooltip", false);

        FOOD_OVERLAY = BUILDER
                .comment("If enabled, the slot containing the food will glow in a color matching to its status")
                .translation("config.notjustspoiled.tooltip.food_overlay")
                .define("foodOverlay", true);

        SHOW_REMAINING_DAYS = BUILDER
                .comment("Shows the number of game days until the food changes to the next status")
                .translation("config.notjustspoiled.tooltip.show_remaining_days")
                .define("showRemainingDays", true);

        SHOW_REMAINING_DAYS_TO_BEING_SPOILED = BUILDER
                .comment("Shows the number of game days until the food becomes spoiled")
                .translation("config.notjustspoiled.tooltip.show_remaining_days_to_being_spoiled")
                .define("showRemainingDaysToBeingSpoiled", false);
        BUILDER.pop();

        CLIENT_CONFIG = BUILDER.build();
    }
}