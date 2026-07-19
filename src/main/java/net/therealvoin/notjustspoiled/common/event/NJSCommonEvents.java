package net.therealvoin.notjustspoiled.common.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.common.data.foodcategory.FoodCategoryReloadListener;
import net.therealvoin.notjustspoiled.common.data.foodstatus.FoodStatusReloadListener;
import net.therealvoin.notjustspoiled.common.foodspoilage.*;
import net.therealvoin.notjustspoiled.common.config.FoodCraftingMode;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;

import java.nio.file.Path;

public class NJSCommonEvents {
    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID)
    public static class ForgeBus {
        // Food spoilage events
        @SubscribeEvent
        public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
            if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
                return;
            }

            if (!(event.getEntity() instanceof ItemEntity itemEntity)) {
                return;
            }

            ItemStack itemEntityStack = itemEntity.getItem();
            BlockFoodSpoilage data = BlockFoodSpoilage.get(serverLevel);
            BlockPos blockPos = itemEntity.blockPosition();
            ItemStack itemStack = data.getLastItemStackAt(blockPos);

            if (!itemStack.isEmpty()) {
                FoodSpoilageManager.copySpoilage(itemStack, itemEntityStack, serverLevel);

                if (serverLevel.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                    data.removeItemStackAt(blockPos, itemStack);
                }
            }

            FoodSpoilageManager.changeEnvironmentAndUpdate(itemEntityStack, FoodEnvironment.OPEN_AIR, serverLevel);
        }

        @SubscribeEvent
        public static void onItemStackedOnOther(ItemStackedOnOtherEvent event) {
            if (!(event.getPlayer().level() instanceof ServerLevel serverLevel)) {
                return;
            }

            ItemStack carriedItem = event.getStackedOnItem();
            ItemStack stackedOnItem = event.getCarriedItem();
            FoodSpoilage foodSpoilage = FoodSpoilage.of(stackedOnItem);

            if (foodSpoilage != null) {
                FoodSpoilageManager.changeEnvironmentAndUpdate(carriedItem, foodSpoilage.getEnvironment(), serverLevel);
            }

            FoodSpoilageManager.tryAverageSpoilageOnMerge(carriedItem, stackedOnItem, serverLevel);
        }

        @SubscribeEvent
        public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
            if (!(event.getEntity().level() instanceof ServerLevel serverLevel)) {
                return;
            }

            ItemStack craftedItem = event.getCrafting();
            FoodSpoilage craftedFoodSpoilage = FoodSpoilage.of(craftedItem);

            if (craftedFoodSpoilage == null) {
                return;
            }

            FoodCraftingMode craftingMode = NJSServerConfig.FOOD_CRAFTING_MODE.get();

            double totalSpoilagePercent = 0;
            int totalCount = 0;
            double worstPercent = 0;

            FoodStatus firstStatus = null;
            boolean hasMixedStatuses = false;

            for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
                ItemStack itemStack = event.getInventory().getItem(i);

                FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);
                if (foodSpoilage == null) {
                    continue;
                }

                double spoilagePercent = FoodSpoilageManager.calculateActualFoodLifetime(foodSpoilage, serverLevel) / FoodCategory.of(itemStack).getSpoilageTime();

                totalSpoilagePercent += spoilagePercent;
                worstPercent = Math.max(worstPercent, spoilagePercent);
                totalCount++;

                FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(itemStack, serverLevel);

                if (firstStatus == null) {
                    firstStatus = foodStatus;
                } else if (firstStatus != foodStatus) {
                    hasMixedStatuses = true;
                }
            }

            if (totalCount == 0) {
                return;
            }

            double finalSpoilagePercent = craftingMode.getCalculationMode(hasMixedStatuses) == FoodCraftingMode.CalculationMode.AVERAGE
                    ? totalSpoilagePercent / totalCount
                    : worstPercent;

            craftedFoodSpoilage.setFoodLifetime(finalSpoilagePercent * FoodCategory.of(craftedItem).getSpoilageTime());
            craftedFoodSpoilage.setEnvironment(FoodEnvironment.STORAGE);
            craftedFoodSpoilage.setLastUpdateTime(serverLevel.getGameTime());
        }

        @SubscribeEvent
        public static void registerDataDriven(AddReloadListenerEvent event) {
            event.addListener(new FoodStatusReloadListener());
            event.addListener(new FoodCategoryReloadListener());
        }
    }

    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        // Config events
        @SubscribeEvent
        public static void onConfigLoad(ModConfigEvent.Loading event) {
            validateChances(event);
        }

        @SubscribeEvent
        public static void onConfigReload(ModConfigEvent.Reloading event) {
            validateChances(event);
        }

        // Datapack event
        @SubscribeEvent
        public static void addCustomDatapack(AddPackFindersEvent event) {
            if (event.getPackType() != PackType.SERVER_DATA) {
                return;
            }

            Path path = ModList.get().getModFileById(NotJustSpoiled.MOD_ID).getFile().findResource("datapack");
            Pack.ResourcesSupplier supplier = id -> new PathPackResources(NotJustSpoiled.MOD_ID, path, true);
            Pack.Info info = new Pack.Info(Component.translatable("datapack.notjustspoiled.description", NotJustSpoiled.MOD_NAME), 15, 15, FeatureFlagSet.of(), true);

            Pack pack =  Pack.create(
                    NotJustSpoiled.MOD_ID,
                    Component.literal(NotJustSpoiled.MOD_NAME),
                    true,
                    supplier,
                    info,
                    PackType.SERVER_DATA,
                    Pack.Position.TOP,
                    true,
                    PackSource.BUILT_IN
            );

            event.addRepositorySource(source -> source.accept(pack));
        }

        private static void validateChances(ModConfigEvent event) {
            if (event.getConfig().getSpec() != NJSServerConfig.CONFIG) {
                return;
            }

            double fresh = NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE.get();
            double stale = NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE.get();
            double halfSpoiled = NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_HALF_SPOILED_FOOD_IN_STORAGE.get();
            double spoiled = NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_SPOILED_FOOD_IN_STORAGE.get();
            double remaining = 1.0;

            fresh = Math.min(fresh, remaining);
            remaining -= fresh;
            stale = Math.min(stale, remaining);
            remaining -= stale;
            halfSpoiled = Math.min(halfSpoiled, remaining);
            remaining -= halfSpoiled;
            spoiled = Math.min(spoiled, remaining);
            remaining -= spoiled;

            if (remaining > 0) {
                fresh += remaining;
            }

            NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE.set(fresh);
            NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE.set(stale);
            NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_HALF_SPOILED_FOOD_IN_STORAGE.set(halfSpoiled);
            NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_SPOILED_FOOD_IN_STORAGE.set(spoiled);


            fresh = NJSServerConfig.FRESH_OR_STALE$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE.get();
            stale = NJSServerConfig.FRESH_OR_STALE$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE.get();
            remaining = 1.0;

            fresh = Math.min(fresh, remaining);
            remaining -= fresh;
            stale = Math.min(stale, remaining);
            remaining -= stale;

            if (remaining > 0) {
                fresh += remaining;
            }

            NJSServerConfig.FRESH_OR_STALE$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE.set(fresh);
            NJSServerConfig.FRESH_OR_STALE$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE.set(stale);
        }
    }
}