package net.therealvoin.notjustspoiled.client.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.common.data.NJSTags;
import net.therealvoin.notjustspoiled.client.config.NJSClientConfig;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;

import java.util.List;

public class NJSClientEvents {
    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, value = Dist.CLIENT)
    public static class ForgeBus {
        private static final Component CATEGORY = Component.translatable("tooltip.notjustspoiled.food_category").append(": ").withStyle(ChatFormatting.GRAY);
        private static final Component STATUS = Component.translatable("tooltip.notjustspoiled.food_status").append(": ").withStyle(ChatFormatting.GRAY);
        private static final Component NEVER_SPOILS = Component.translatable("tooltip.notjustspoiled.never_spoils").withStyle(ChatFormatting.AQUA);
        private static final Component TO_NEXT_STATUS = Component.translatable("tooltip.notjustspoiled.to_next_status").append(": ").withStyle(ChatFormatting.GRAY);
        private static final Component SPOILS_IN = Component.translatable("tooltip.notjustspoiled.spoils_in").append(": ").withStyle(ChatFormatting.GRAY);

        @SubscribeEvent
        public static void addFoodSpoilageInfoToTooltip(ItemTooltipEvent event) {
            Player player = event.getEntity();

            if (player == null) {
                return;
            }

            ItemStack tooltipItem = event.getItemStack();
            List<Component> tooltip = event.getToolTip();

            if (tooltipItem.is(NJSTags.Items.ALWAYS_SPOILED)) {
                tooltip.add(STATUS.copy().append(FoodStatus.SPOILED.getDisplayName()));
                return;
            } else if (tooltipItem.is(NJSTags.Items.NEVER_SPOILS)) {
                tooltip.add(STATUS.copy().append(NEVER_SPOILS));
                return;
            }

            FoodCategory tooltipItemCategory = FoodCategory.of(tooltipItem);
            Level level = player.level();

            if (NJSClientConfig.SHOW_FOOD_CATEGORY.get()) {
                if (tooltipItemCategory != null) {
                    tooltip.add(CATEGORY.copy().append(tooltipItemCategory.getDisplayName()));
                }
            }

            FoodStatus currentFoodStatus = FoodSpoilageManager.getFoodStatus(tooltipItem, level);
            if (currentFoodStatus == null) {
                return;
            }

            tooltip.add(STATUS.copy().append(currentFoodStatus.getDisplayName()));

            FoodSpoilage foodSpoilage = FoodSpoilage.of(tooltipItem);
            FoodStatus nextFoodStatus = currentFoodStatus.getNext();

            if (currentFoodStatus != FoodStatus.SPOILED) {
                if (NJSClientConfig.SHOW_REMAINING_DAYS_TO_NEXT_FOOD_STATUS.get() && nextFoodStatus != FoodStatus.SPOILED) {
                    tooltip.add((createTranslationLine(TO_NEXT_STATUS, currentFoodStatus.getEnd(tooltipItemCategory.getSpoilageTime()), foodSpoilage, level)));
                }

                if (NJSClientConfig.SHOW_REMAINING_DAYS_TO_SPOILED_STATUS.get() || NJSClientConfig.SHOW_REMAINING_DAYS_TO_NEXT_FOOD_STATUS.get() && nextFoodStatus == FoodStatus.SPOILED) {
                    tooltip.add(createTranslationLine(SPOILS_IN, tooltipItemCategory.getSpoilageTime(), foodSpoilage, level));
                }
            }

            if (NJSClientConfig.SHOW_DEBUG_INFO.get()) {
                tooltip.add(Component.literal("lastUpdateTime: ").append(String.valueOf(foodSpoilage.getLastUpdateTime())));
                tooltip.add(Component.literal("foodLifetime: ").append(String.valueOf(foodSpoilage.getFoodLifetime())));
                tooltip.add(Component.literal("environment: ").append(foodSpoilage.getEnvironment().name()));
            }
        }

        private static Component createTranslationLine(Component component, double someNumber, FoodSpoilage foodSpoilage, Level level) {
            int days = (int) Math.floor((someNumber - FoodSpoilageManager.calculateActualFoodLifetime(foodSpoilage, level)) / foodSpoilage.getEnvironment().getFoodSpoilageMultiplier()) / 24000;

            if (days > 0) {
                return component.copy().append(Component.translatable("tooltip.notjustspoiled.to_next_status.days", days));
            }

            return component.copy().append(Component.translatable("tooltip.notjustspoiled.to_next_status.one_day"));
        }
    }

    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBus {
        private static final int OVERLAY_ALPHA = 0x80000000;

        @SubscribeEvent
        public static void registerFoodSlotOverlay(RegisterItemDecorationsEvent event) {
            for (Item item : ForgeRegistries.ITEMS) {
                event.register(item, ModBus::renderFoodSlotOverlay);
            }
        }

        private static boolean renderFoodSlotOverlay(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
            if (!NJSClientConfig.FOOD_SLOT_OVERLAY.get()) {
                return false;
            }

            Minecraft minecraft = Minecraft.getInstance();

            // We don't need to render overlay if item is carried by player as it looks weird
            ItemStack carriedStack = minecraft.player.containerMenu.getCarried();
            if (carriedStack == stack) {
                return false;
            }

            if (stack.is(NJSTags.Items.ALWAYS_SPOILED)) {
                drawOverlay(guiGraphics, xOffset, yOffset, FoodStatus.SPOILED.getColor());
                return true;
            } else if (stack.is(NJSTags.Items.NEVER_SPOILS)) {
                drawOverlay(guiGraphics, xOffset, yOffset, ChatFormatting.AQUA);
                return true;
            }

            FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(stack, minecraft.level);
            if (foodStatus == null) {
                return false;
            }

            drawOverlay(guiGraphics, xOffset, yOffset, foodStatus.getColor());
            return true;
        }

        private static void drawOverlay(GuiGraphics guiGraphics, int xOffset, int yOffset, ChatFormatting chatFormatting) {
            guiGraphics.fill(xOffset, yOffset, xOffset + 16, yOffset + 16, OVERLAY_ALPHA | chatFormatting.getColor());
        }
    }
}