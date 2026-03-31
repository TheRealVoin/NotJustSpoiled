package net.therealvoin.notjustspoiled.core;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.core.config.CraftingMode;
import net.therealvoin.notjustspoiled.core.config.NJSConfig;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodStatus;
import net.therealvoin.notjustspoiled.core.foodspoilage.capability.blockfoodspoilage.BlockFoodSpoilageProvider;
import net.therealvoin.notjustspoiled.core.foodspoilage.capability.foodspoilage.FoodSpoilageProvider;
import net.therealvoin.notjustspoiled.core.foodspoilage.capability.foodspoilage.IFoodSpoilage;
import net.therealvoin.notjustspoiled.util.NJSUtils;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

import java.util.List;

public class NJSEvents {
    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, value = Dist.CLIENT)
    public static class Client {
        private static final Component STATUS = Component.translatable("tooltip.notjustspoiled.status").append(": ").withStyle(ChatFormatting.GRAY);
        private static final Component NEVER_SPOILS = Component.translatable("tooltip.notjustspoiled.never_spoils").withStyle(ChatFormatting.AQUA);

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

            // For debug
            // tooltip.add(Component.literal("Тег: " + (FoodCategory.getFoodCategory(tooltipItem) != null)));

            if (event.getEntity() == null) {
                return;
            }

            FoodStatus currentFoodStatus = FoodSpoilageManager.getFoodStatusForTooltip(tooltipItem, event.getEntity().level());
            if (currentFoodStatus == null) {
                return;
            }

            tooltip.add(STATUS.copy().append(currentFoodStatus.getTranslation()));

            // For debug
//            IFoodSpoilage foodSpoilage = NJSUtils.getCapability(tooltipItem);
//            tooltip.add(Component.literal("lastUpdateTime: ").append(String.valueOf(foodSpoilage.getLastUpdateTime())));
//            tooltip.add(Component.literal("foodLifetime: ").append(String.valueOf(foodSpoilage.getFoodLifetime())));
//            tooltip.add(Component.literal("environment: ").append(foodSpoilage.getEnvironment().name()));
        }
    }

    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID)
    public static class CommonForge {
        private static final ResourceLocation FOOD_SPOILAGE = ResourceLocation.fromNamespaceAndPath(NotJustSpoiled.MOD_ID, "food_spoilage");
        private static final ResourceLocation BLOCK_FOOD_SPOILAGE = ResourceLocation.fromNamespaceAndPath(NotJustSpoiled.MOD_ID, "block_food_spoilage");

        // Food spoilage events
        @SubscribeEvent
        public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof ItemEntity itemEntity) {
                event.getLevel().getCapability(BlockFoodSpoilageProvider.BLOCK_FOOD_SPOILAGE).ifPresent(blockFoodSpoilage ->{
                    BlockPos blockPos = BlockPos.containing(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
                    ItemStack itemStack = blockFoodSpoilage.getItemStackByPos(blockPos);
                    if (itemStack == null) {
                        return;
                    }

                    NJSUtils.copyCapability(itemStack, itemEntity.getItem());
                    if (itemEntity.level().getBlockState(blockPos).getBlock() == Blocks.AIR) {
                        blockFoodSpoilage.removeItemStackPos(blockPos);
                    }
                });
                FoodSpoilageManager.changeEnvironmentAndUpdate(itemEntity.getItem(), FoodEnvironment.GROUND, itemEntity.level());
            }
        }

        @SubscribeEvent
        public static void onItemStackedOnOther(ItemStackedOnOtherEvent event) {
            Level level = event.getPlayer().level();
            if (level.isClientSide()) {
                return;
            }

            ItemStack carriedItem = event.getCarriedItem();
            ItemStack stackedOnItem = event.getStackedOnItem();

            if (!carriedItem.isEmpty() && !stackedOnItem.isEmpty()) {
                carriedItem.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
                    FoodSpoilageManager.changeEnvironmentAndUpdate(stackedOnItem, foodSpoilage.getEnvironment(), level);
                });
                FoodSpoilageManager.tryAverageSpoilageOnMerge(stackedOnItem, carriedItem, level);
            }
        }

        @SubscribeEvent
        public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
            Level level = event.getEntity().level();
            ItemStack craftedItem = event.getCrafting();
            FoodCategory craftedItemCategory = FoodCategory.getFoodCategory(craftedItem);

            if (level.isClientSide() || craftedItemCategory == null) {
                return;
            }

            double totalSpoilagePercent = 0;
            int count = 0;
            if (NJSConfig.CRAFTING_MODE.get() == CraftingMode.AVERAGE) {
                for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
                    ItemStack itemStack = event.getInventory().getItem(i);
                    FoodCategory itemStackCategory = FoodCategory.getFoodCategory(itemStack);
                    if (itemStack.isEmpty() || itemStackCategory == null) {
                        continue;
                    }

                    IFoodSpoilage foodSpoilage = NJSUtils.getCapability(itemStack);
                    if (foodSpoilage == null) {
                        continue;
                    }

                    FoodSpoilageManager.updateFoodLifetime(foodSpoilage, level);
                    totalSpoilagePercent += foodSpoilage.getFoodLifetime() / itemStackCategory.getSpoilageTime();
                    count++;
                }

                if (count == 0) {
                    return;
                }

                double finalSpoilagePercent = totalSpoilagePercent / count;
                double finalFoodLifetime = finalSpoilagePercent * FoodCategory.getFoodCategory(craftedItem).getSpoilageTime();
                craftedItem.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
                    foodSpoilage.setFoodLifetime(finalFoodLifetime);
                    foodSpoilage.setEnvironment(FoodEnvironment.STORAGE);
                    foodSpoilage.setLastUpdateTime(level.getGameTime());
                });
            } else if (NJSConfig.CRAFTING_MODE.get() == CraftingMode.WORST_STATUS) {
                double worstPercent = 0;
                for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
                    ItemStack itemStack = event.getInventory().getItem(i);

                    FoodCategory itemStackCategory = FoodCategory.getFoodCategory(itemStack);
                    if (itemStack.isEmpty() || itemStackCategory == null) {
                        continue;
                    }

                    IFoodSpoilage foodSpoilage = NJSUtils.getCapability(itemStack);
                    if (foodSpoilage == null) {
                        continue;
                    }

                    FoodSpoilageManager.updateFoodLifetime(foodSpoilage, level);
                    double percent = foodSpoilage.getFoodLifetime() / itemStackCategory.getSpoilageTime();

                    if (percent > worstPercent) {
                        worstPercent = percent;
                    }
                }

                double finalFoodLifeTime = worstPercent * craftedItemCategory.getSpoilageTime();

                craftedItem.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
                    foodSpoilage.setFoodLifetime(finalFoodLifeTime);
                    foodSpoilage.setEnvironment(FoodEnvironment.STORAGE);
                    foodSpoilage.setLastUpdateTime(level.getGameTime());
                });
            }
        }

        // Capability events
        @SubscribeEvent
        public static void attachFoodSpoilageToFood(AttachCapabilitiesEvent<ItemStack> event) {
            if (FoodCategory.getFoodCategory(event.getObject()) == null) {
                return;
            }

            event.addCapability(FOOD_SPOILAGE, new FoodSpoilageProvider());
        }

        @SubscribeEvent
        public static void attachFoodSpoilageToBlock(AttachCapabilitiesEvent<Level> event) {
            if (event.getObject().isClientSide()) {
                return;
            }

            event.addCapability(BLOCK_FOOD_SPOILAGE, new BlockFoodSpoilageProvider());
        }

        // AppleSkin integration
        @SubscribeEvent
        public static void onFoodValues(FoodValuesEvent event) {
            FoodStatus foodStatus = FoodSpoilageManager.getFoodStatusForTooltip(event.itemStack, event.player.level());
            if (foodStatus == null) {
                return;
            }

            int defaultNutrition = event.defaultFoodValues.hunger;
            float defaultSaturationModifier = event.defaultFoodValues.saturationModifier;
            event.modifiedFoodValues = new FoodValues(foodStatus.getModifiedNutrition(defaultNutrition), foodStatus.getModifiedSaturation(defaultSaturationModifier));
        }
    }

    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonMod {
        // Config events
        @SubscribeEvent
        public static void onConfigLoad(ModConfigEvent.Loading event) {
            NJSUtils.validateChances(event);
        }

        @SubscribeEvent
        public static void onConfigReload(ModConfigEvent.Reloading event) {
            NJSUtils.validateChances(event);
        }
    }
}