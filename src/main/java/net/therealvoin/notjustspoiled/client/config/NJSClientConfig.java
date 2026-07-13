package net.therealvoin.notjustspoiled.client.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NJSClientConfig {
    public static final ForgeConfigSpec CONFIG;

    public static final ForgeConfigSpec.BooleanValue SHOW_FOOD_CATEGORY;
    public static final ForgeConfigSpec.BooleanValue SHOW_REMAINING_DAYS_TO_NEXT_FOOD_STATUS;
    public static final ForgeConfigSpec.BooleanValue SHOW_REMAINING_DAYS_TO_SPOILED_STATUS;

    public static final ForgeConfigSpec.BooleanValue FOOD_SLOT_OVERLAY;

    public static final ForgeConfigSpec.BooleanValue SHOW_DEBUG_INFO;
    public static final ForgeConfigSpec.BooleanValue SEND_DEBUG_MESSAGE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.translation("config.notjustspoiled.tooltip").push("Tooltip");
        SHOW_FOOD_CATEGORY = builder
                .comment("Shows the food category in the tooltip if present.")
                .translation("config.notjustspoiled.tooltip.show_food_category")
                .define("showFoodCategory", false);

        SHOW_REMAINING_DAYS_TO_NEXT_FOOD_STATUS = builder
                .comment("Shows the number of in-game days until the food reaches the next status.")
                .translation("config.notjustspoiled.tooltip.show_remaining_days_to_next_food_status")
                .define("showRemainingDaysToNextFoodStatus", true);

        SHOW_REMAINING_DAYS_TO_SPOILED_STATUS = builder
                .comment("Shows the number of in-game days until the food becomes spoiled.")
                .translation("config.notjustspoiled.tooltip.show_remaining_days_to_spoiled_status")
                .define("showRemainingDaysToSpoiledStatus", false);
        builder.pop();

        builder.translation("config.notjustspoiled.visuals").push("Visuals");
        FOOD_SLOT_OVERLAY = builder
                .comment("Makes the food slot glow with a color corresponding to the food's status.")
                .translation("config.notjustspoiled.visuals.food_slot_overlay")
                .define("foodSlotOverlay", true);
        builder.pop();

        builder.translation("config.notjustspoiled.debug").push("Debug");
        SHOW_DEBUG_INFO = builder
                .comment("Shows additional debug information in the tooltip.")
                .translation("config.notjustspoiled.debug.show_debug_info")
                .define("showDebugInfo", false);

        SEND_DEBUG_MESSAGE = builder
                .comment("Sends a debug message to the chat when food is updated or initialized.")
                .translation("config.notjustspoiled.debug.send_debug_message")
                .define("sendDebugMessage", false);
        builder.pop();

        CONFIG = builder.build();
    }
}