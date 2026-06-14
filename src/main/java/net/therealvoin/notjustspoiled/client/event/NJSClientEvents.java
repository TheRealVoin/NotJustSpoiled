package net.therealvoin.notjustspoiled.client.event;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.foodspoilage.FoodSpoilageProvider;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.foodspoilage.IFoodSpoilage;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.slf4j.Logger;

import java.util.List;

public class NJSClientEvents {
    public static final Logger LOGGER = LogUtils.getLogger();

    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, value = Dist.CLIENT)
    public static class ForgeBus {
        private static final Component STATUS = Component.translatable("tooltip.notjustspoiled.status").append(": ").withStyle(ChatFormatting.GRAY);
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

            if (NJSClientConfig.SHOW_CATEGORY_IN_TOOLTIP.get()) {
                FoodCategory foodCategory = FoodCategory.getFoodCategory(tooltipItem);
                if (foodCategory != null) {
                    Component category = Component.literal(String.valueOf(foodCategory)).withStyle(ChatFormatting.LIGHT_PURPLE);
                    tooltip.add(CATEGORY.copy().append(category));
                }
            }

            FoodStatus currentFoodStatus = FoodSpoilageManager.getFoodStatus(tooltipItem, event.getEntity().level());

            tooltipItem.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
                LOGGER.info("lastUpdateTime = {}", foodSpoilage.getLastUpdateTime());
                LOGGER.info("foodLifetime = {}", foodSpoilage.getFoodLifetime());
                LOGGER.info("environment = {}", foodSpoilage.getEnvironment());
            });

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

    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBus {
        @SubscribeEvent
        public static void changeSlotColorBasedOnFoodStatus(RegisterItemDecorationsEvent event) {
            for (Item item : ForgeRegistries.ITEMS) {
                event.register(item, (guiGraphics, font, stack, xOffset, yOffset) -> {
                    if (NJSClientConfig.FOOD_OVERLAY.get()) {
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