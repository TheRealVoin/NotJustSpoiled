package net.therealvoin.notjustspoiled.core.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.data.NJSTags;
import net.therealvoin.notjustspoiled.core.config.NJSClientConfig;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodStatus;
import net.therealvoin.notjustspoiled.core.foodspoilage.capability.foodspoilage.IFoodSpoilage;
import net.therealvoin.notjustspoiled.util.NJSUtils;

import java.util.List;

@Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, value = Dist.CLIENT)
public class NJSClientEvents {
    private static final Component STATUS = Component.translatable("tooltip.notjustspoiled.status").append(": ").withStyle(ChatFormatting.GRAY);
    private static final Component NEVER_SPOILS = Component.translatable("tooltip.notjustspoiled.never_spoils").withStyle(ChatFormatting.AQUA);
    private static final Component CATEGORY = Component.translatable("tooltip.notjustspoiled.debug.category").append(": ").withStyle(ChatFormatting.GRAY);

    @SubscribeEvent
    public static void addFoodStatusToTooltip(ItemTooltipEvent event) {
        ItemStack tooltipItem = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        if (tooltipItem.is(NJSTags.Items.ALWAYS_SPOILED)) {
            tooltip.add(STATUS.copy().append(FoodStatus.SPOILED.getTranslation()));
            return;
        } else if (tooltipItem.is(NJSTags.Items.NEVER_SPOILS)) {
            tooltip.add(STATUS.copy().append(NEVER_SPOILS));
            return;
        }

        if (NJSClientConfig.SHOW_CATEGORY_IN_TOOLTIP.get()) {
            FoodCategory foodCategory = FoodCategory.getFoodCategory(tooltipItem);
            Component category = Component.literal(String.valueOf(foodCategory)).withStyle(ChatFormatting.LIGHT_PURPLE);
            if (foodCategory == null) {
                category = category.copy().withStyle(ChatFormatting.GOLD);
            }
            tooltip.add(CATEGORY.copy().append(category));
        }

        FoodStatus currentFoodStatus = FoodSpoilageManager.getFoodStatus(tooltipItem, Minecraft.getInstance().level);
        if (currentFoodStatus == null) {
            return;
        }

        tooltip.add(STATUS.copy().append(currentFoodStatus.getTranslation()));

        if (NJSClientConfig.SHOW_ADDITIONAL_INFO.get()) {
            IFoodSpoilage foodSpoilage = NJSUtils.getCapability(tooltipItem);
            tooltip.add(Component.literal("lastUpdateTime: ").append(String.valueOf(foodSpoilage.getLastUpdateTime())));
            tooltip.add(Component.literal("foodLifetime: ").append(String.valueOf(foodSpoilage.getFoodLifetime())));
            tooltip.add(Component.literal("environment: ").append(foodSpoilage.getEnvironment().name()));
        }
    }
}