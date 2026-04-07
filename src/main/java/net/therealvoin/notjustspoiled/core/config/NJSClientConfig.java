package net.therealvoin.notjustspoiled.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NJSClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec.Builder BUILDER;

    public static final ForgeConfigSpec.BooleanValue SHOW_CATEGORY_IN_TOOLTIP;
    public static final ForgeConfigSpec.BooleanValue SHOW_ADDITIONAL_INFO;

    static {
        BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.translation("config.notjustspoiled.category.debug").push("Debug");
        SHOW_CATEGORY_IN_TOOLTIP = BUILDER
                .comment("Shows food category in tooltip. If food doesn't have category, then \"null\" will be displayed.")
                .translation("config.notjustspoiled.debug.show_food_category")
                .define("showCategoryInTooltip", false);

        SHOW_ADDITIONAL_INFO = BUILDER
                .comment("Shows additional food info in tooltip (\"lastUpdateTime\", \"foodLifeTime\" and \"environment\").")
                .translation("config.notjustspoiled.debug.show_additional_info")
                .define("showAdditionalInfo", false);

        BUILDER.pop();

        CLIENT_CONFIG = BUILDER.build();
    }
}