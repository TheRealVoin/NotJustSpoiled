package net.therealvoin.notjustspoiled.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.PacketDistributor;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.data.foodcategory.FoodCategoryData;
import net.therealvoin.notjustspoiled.data.foodcategory.FoodCategoryReloadListener;
import net.therealvoin.notjustspoiled.data.foodstatus.FoodStatusData;
import net.therealvoin.notjustspoiled.data.foodstatus.FoodStatusReloadListener;
import net.therealvoin.notjustspoiled.data.recipe.SpoiledFoodBrewingRecipe;
import net.therealvoin.notjustspoiled.foodspoilage.*;
import net.therealvoin.notjustspoiled.config.NJSServerConfig;
import net.therealvoin.notjustspoiled.foodspoilage.*;
import net.therealvoin.notjustspoiled.network.NJSNetwork;
import net.therealvoin.notjustspoiled.network.SyncFoodCategoryDataPacket;
import net.therealvoin.notjustspoiled.network.SyncFoodStatusDataPacket;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

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
        public static void registerDataDriven(AddReloadListenerEvent event) {
            event.addListener(new FoodStatusReloadListener());
            event.addListener(new FoodCategoryReloadListener());
        }

        @SubscribeEvent
        public static void onDatapackSync(OnDatapackSyncEvent event) {
            Map<FoodStatus, FoodStatusData> statusDataMap = new EnumMap<>(FoodStatus.class);
            Map<FoodCategory, FoodCategoryData> categoryDataMap = new EnumMap<>(FoodCategory.class);

            for (FoodStatus foodStatus : FoodStatus.values()) {
                statusDataMap.put(foodStatus, foodStatus.getServerData());
            }

            for (FoodCategory foodfoodCategory : FoodCategory.values()) {
                categoryDataMap.put(foodfoodCategory, foodfoodCategory.getServerData());
            }

            SyncFoodStatusDataPacket statusPacket = new SyncFoodStatusDataPacket(statusDataMap);
            SyncFoodCategoryDataPacket categoryPacket = new SyncFoodCategoryDataPacket(categoryDataMap);

            for (ServerPlayer player : event.getPlayers()) {
                NJSNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), statusPacket);
                NJSNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), categoryPacket);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void registerBrewingRecipe(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new SpoiledFoodBrewingRecipe()));
        }

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

            NJSServerConfig.bakeConfig();

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