package net.therealvoin.notjustspoiled.common.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.common.config.FoodCraftingMode;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.blockfoodspoilage.BlockFoodSpoilageProvider;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.foodspoilage.FoodSpoilageProvider;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.foodspoilage.IFoodSpoilage;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;

import java.nio.file.Path;

public class NJSCommonEvents {
    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID)
    public static class ForgeBus {
        private static final ResourceLocation FOOD_SPOILAGE = ResourceLocation.fromNamespaceAndPath(NotJustSpoiled.MOD_ID, "food_spoilage");
        private static final ResourceLocation BLOCK_FOOD_SPOILAGE = ResourceLocation.fromNamespaceAndPath(NotJustSpoiled.MOD_ID, "block_food_spoilage");

        // Food spoilage events
        @SubscribeEvent
        public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof ItemEntity itemEntity) {
                if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
                    return;
                }

                ItemStack itemEntityStack = itemEntity.getItem();

                serverLevel.getCapability(BlockFoodSpoilageProvider.BLOCK_FOOD_SPOILAGE).ifPresent(blockFoodSpoilage -> {
                    BlockPos blockPos = itemEntity.blockPosition();
                    ItemStack itemStack = blockFoodSpoilage.getLastItemStackByPos(blockPos);
                    if (itemStack.isEmpty()) {
                        return;
                    }

                    NJSUtils.copyCapability(itemStack, itemEntityStack, serverLevel);
                    if (itemEntity.level().getBlockState(blockPos).getBlock() == Blocks.AIR) {
                        blockFoodSpoilage.removeItemStackPos(blockPos, itemStack);
                    }
                });

                FoodSpoilageManager.changeEnvironmentAndUpdate(itemEntityStack, FoodEnvironment.GROUND, serverLevel);
            }
        }

        @SubscribeEvent
        public static void onItemStackedOnOther(ItemStackedOnOtherEvent event) {
            if (event.getPlayer().level() instanceof ServerLevel serverLevel) {
                ItemStack carriedItem = event.getStackedOnItem();
                ItemStack stackedOnItem = event.getCarriedItem();

                if (!carriedItem.isEmpty() && !stackedOnItem.isEmpty()) {
                    stackedOnItem.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
                        FoodSpoilageManager.changeEnvironmentAndUpdate(carriedItem, foodSpoilage.getEnvironment(), serverLevel);
                    });

                    FoodSpoilageManager.tryAverageSpoilageOnMerge(carriedItem, stackedOnItem, serverLevel);
                }
            }
        }

        @SubscribeEvent
        public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
            if (!(event.getEntity().level() instanceof ServerLevel serverLevel)) {
                return;
            }

            ItemStack craftedItem = event.getCrafting();
            FoodCategory craftedItemCategory = FoodCategory.getFoodCategory(craftedItem);

            if (craftedItemCategory == null) {
                return;
            }

            double totalSpoilagePercent = 0;
            int count = 0;

            FoodCraftingMode craftingMode = NJSServerConfig.FOOD_CRAFTING_MODE.get();
            if (craftingMode == FoodCraftingMode.AVERAGE || craftingMode == FoodCraftingMode.SAME_STATUS) {
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

                    FoodSpoilageManager.updateFoodLifetime(foodSpoilage, serverLevel);
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
                    foodSpoilage.setLastUpdateTime(serverLevel.getGameTime());
                });
            } else if (NJSServerConfig.FOOD_CRAFTING_MODE.get() == FoodCraftingMode.WORST_STATUS) {
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

                    FoodSpoilageManager.updateFoodLifetime(foodSpoilage, serverLevel);
                    double percent = foodSpoilage.getFoodLifetime() / itemStackCategory.getSpoilageTime();

                    if (percent > worstPercent) {
                        worstPercent = percent;
                    }
                }

                double finalFoodLifeTime = worstPercent * craftedItemCategory.getSpoilageTime();

                craftedItem.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
                    foodSpoilage.setFoodLifetime(finalFoodLifeTime);
                    foodSpoilage.setEnvironment(FoodEnvironment.STORAGE);
                    foodSpoilage.setLastUpdateTime(serverLevel.getGameTime());
                });
            }
        }

        // Capability events
        @SubscribeEvent
        public static void attachFoodSpoilageToFood(AttachCapabilitiesEvent<ItemStack> event) {
            if (FoodCategory.getFoodCategory(event.getObject()) != null) {
                event.addCapability(FOOD_SPOILAGE, new FoodSpoilageProvider());
            }
        }

        @SubscribeEvent
        public static void attachFoodSpoilageToBlock(AttachCapabilitiesEvent<Level> event) {
            if (event.getObject() instanceof ServerLevel) {
                event.addCapability(BLOCK_FOOD_SPOILAGE, new BlockFoodSpoilageProvider());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = NotJustSpoiled.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        // Config events
        @SubscribeEvent
        public static void onConfigLoad(ModConfigEvent.Loading event) {
            NJSUtils.validateChances(event);
        }

        @SubscribeEvent
        public static void onConfigReload(ModConfigEvent.Reloading event) {
            NJSUtils.validateChances(event);
        }

        // Datapack event
        @SubscribeEvent
        public static void addCustomDatapack(AddPackFindersEvent event) {
            if (event.getPackType() != PackType.SERVER_DATA) {
                return;
            }

            Path path = ModList.get().getModFileById(NotJustSpoiled.MOD_ID).getFile().findResource("datapack");
            Pack.ResourcesSupplier supplier = id -> new PathPackResources(NotJustSpoiled.MOD_ID, path, true);
            Pack.Info info = new Pack.Info(Component.translatable("datapack.notjustspoiled.description"), 15, 15, FeatureFlagSet.of(), true);

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
    }
}