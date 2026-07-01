package net.therealvoin.notjustspoiled.client.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
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
        private static final Component STATUS = Component.translatable("tooltip.notjustspoiled.status").append(": ").withStyle(ChatFormatting.GRAY);
        private static final Component TO_NEXT_STATUS = Component.translatable("tooltip.notjustspoiled.to_next_status").append(": ").withStyle(ChatFormatting.GRAY);
        private static final Component SPOILS_IN = Component.translatable("tooltip.notjustspoiled.spoils_in").append(": ").withStyle(ChatFormatting.GRAY);
        private static final Component NEVER_SPOILS = Component.translatable("tooltip.notjustspoiled.never_spoils").withStyle(ChatFormatting.AQUA);
        private static final Component CATEGORY = Component.translatable("tooltip.notjustspoiled.debug.category").append(": ").withStyle(ChatFormatting.GRAY);

        @SubscribeEvent
        public static void addFoodStatusToTooltip(ItemTooltipEvent event) {
            if (event.getEntity() == null) {
                return;
            }

            ItemStack tooltipItem = event.getItemStack();
            List<Component> tooltip = event.getToolTip();

            if (tooltipItem.is(NJSTags.Items.ALWAYS_SPOILED)) {
                tooltip.add(STATUS.copy().append(FoodStatus.SPOILED.getTranslation()));
                return;
            } else if (tooltipItem.is(NJSTags.Items.NEVER_SPOILS)) {
                tooltip.add(STATUS.copy().append(NEVER_SPOILS));
                return;
            }

            FoodCategory foodCategory = FoodCategory.getFoodCategory(tooltipItem);
            if (NJSClientConfig.SHOW_CATEGORY_IN_TOOLTIP.get()) {
                if (foodCategory != null) {
                    Component category = Component.literal(String.valueOf(foodCategory)).withStyle(ChatFormatting.LIGHT_PURPLE);
                    tooltip.add(CATEGORY.copy().append(category));
                }
            }

            Level level = event.getEntity().level();

            FoodStatus currentFoodStatus = FoodSpoilageManager.getFoodStatus(tooltipItem, level);
            if (currentFoodStatus == null) {
                return;
            }

            tooltip.add(STATUS.copy().append(currentFoodStatus.getTranslation()));

            boolean showNextStatus = false;
            boolean showSpoilsIn = false;

            if (currentFoodStatus != FoodStatus.SPOILED) {
                if (currentFoodStatus == FoodStatus.HALF_SPOILED) {
                    showSpoilsIn = NJSClientConfig.SHOW_REMAINING_DAYS.get() || NJSClientConfig.SHOW_REMAINING_DAYS_TO_BEING_SPOILED.get();
                } else {
                    showNextStatus = NJSClientConfig.SHOW_REMAINING_DAYS.get();
                    showSpoilsIn = NJSClientConfig.SHOW_REMAINING_DAYS_TO_BEING_SPOILED.get();
                }
            }

            if (showNextStatus) {
                FoodStatus next = currentFoodStatus.getNext();
                if (next != null) {
                    double remainingTime = currentFoodStatus.getThreshold(foodCategory.getSpoilageTime()) - FoodSpoilageManager.calculateActualFoodLifetime(FoodSpoilage.of(tooltipItem), level);
                    int totalTicks = (int) Math.floor(remainingTime / FoodSpoilage.of(tooltipItem).getEnvironment().getFoodSpoilageMultiplier());
                    int days = totalTicks / 24000;

                    if (days > 0) {
                        tooltip.add(TO_NEXT_STATUS.copy().append(Component.translatable("tooltip.notjustspoiled.to_next_status.days", days).withStyle(ChatFormatting.WHITE)));
                    } else {
                        tooltip.add(TO_NEXT_STATUS.copy().append(Component.translatable("tooltip.notjustspoiled.to_next_status.one_day").withStyle(ChatFormatting.WHITE)));
                    }
                }
            }

            if (showSpoilsIn) {
                double remainingTicks = foodCategory.getSpoilageTime() - FoodSpoilageManager.calculateActualFoodLifetime(FoodSpoilage.of(tooltipItem), level);
                int days = (int) Math.floor(remainingTicks / FoodSpoilage.of(tooltipItem).getEnvironment().getFoodSpoilageMultiplier() / 24000);

                if (days > 0) {
                    tooltip.add(SPOILS_IN.copy().append(Component.translatable("tooltip.notjustspoiled.to_next_status.days", days).withStyle(ChatFormatting.WHITE)));
                } else {
                    tooltip.add(SPOILS_IN.copy().append(Component.translatable("tooltip.notjustspoiled.to_next_status.one_day", days).withStyle(ChatFormatting.WHITE)));
                }
            }

            if (NJSClientConfig.SHOW_ADDITIONAL_INFO.get()) {
                FoodSpoilage foodSpoilage = FoodSpoilage.of(tooltipItem);
                tooltip.add(Component.literal("lastUpdateTime: ").append(String.valueOf(foodSpoilage.getLastUpdateTime())));
                tooltip.add(Component.literal("foodLifetime: ").append(String.valueOf(foodSpoilage.getFoodLifetime())));
                tooltip.add(Component.literal("environment: ").append(foodSpoilage.getEnvironment().name()));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBus {
        @SubscribeEvent
        public static void changeSlotColorBasedOnFoodStatus(RegisterItemDecorationsEvent event) {
            for (Item item : ForgeRegistries.ITEMS) {
                event.register(item, (guiGraphics, font, stack, xOffset, yOffset) -> {
                    if (NJSClientConfig.FOOD_OVERLAY.get()) {
                        if (stack.is(NJSTags.Items.ALWAYS_SPOILED)) {
                            guiGraphics.fill(xOffset, yOffset, xOffset + 16, yOffset + 16, 0x80000000 | ChatFormatting.RED.getColor());
                            return true;
                        } else if (stack.is(NJSTags.Items.NEVER_SPOILS)) {
                            guiGraphics.fill(xOffset, yOffset, xOffset + 16, yOffset + 16, 0x80000000 | ChatFormatting.AQUA.getColor());
                            return true;
                        }

                        FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(stack, Minecraft.getInstance().level);
                        if (foodStatus == null) {
                            return false;
                        }

                        ItemStack carried = Minecraft.getInstance().player.containerMenu.getCarried();
                        if (carried == stack) {
                            return false;
                        }

                        int color = 0x80000000 | foodStatus.getColor().getColor();
                        guiGraphics.fill(xOffset, yOffset, xOffset + 16, yOffset + 16, color);
                        return true;
                    } else {
                        return false;
                    }
                });
            }
        }
    }
}