package net.therealvoin.notjustspoiled.common.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.FoodSpoilageProvider;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.IFoodSpoilage;

import java.util.List;

public class NJSUtils {
    private static long lastWarningMessageTime = 0;

    public static Level getLevelBySlot(Slot slot) {
        Container container = slot.container;
        if (container instanceof Inventory inventory) {
            return inventory.player.level();
        } else if (container instanceof BaseContainerBlockEntity blockEntity) {
            return blockEntity.getLevel();
        } else if (container instanceof AbstractMinecartContainer minecart) {
            return minecart.level();
        } else if (container instanceof ChestBoat chestBoat) {
            return chestBoat.level();
        } else if (container instanceof SimpleContainer simpleContainer) {
            AbstractHorse horse = ((SimpleContainerAccessor) simpleContainer).getHorse();
            if (horse != null) {
                return horse.level();
            }
        }

        return null;
    }

    public static IFoodSpoilage getCapability(ItemStack itemStack) {
        if (FoodCategory.getFoodCategory(itemStack) == null) {
            return null;
        }

        return itemStack.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).resolve().orElse(null);
    }

    public static void copyCapability(ItemStack copyFrom, ItemStack copyTo, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        copyFrom.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage1 -> {
            copyTo.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage2 -> {
                FoodSpoilageManager.updateFoodLifetime(foodSpoilage1, serverLevel);

                double spoilagePercent = foodSpoilage1.getFoodLifetime() / FoodCategory.getFoodCategory(copyFrom).getSpoilageTime();
                foodSpoilage2.setFoodLifetime(spoilagePercent * FoodCategory.getFoodCategory(copyTo).getSpoilageTime());
                foodSpoilage2.setEnvironment(foodSpoilage1.getEnvironment());
                foodSpoilage2.setLastUpdateTime(foodSpoilage1.getLastUpdateTime());
            });
        });
    }

    public static void validateChances(ModConfigEvent event) {
        if (event.getConfig().getSpec() != NJSServerConfig.SERVER_CONFIG) {
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

    public static void sendWarningMessage(ServerLevel level) {
        List<ServerPlayer> players = level.getServer().getPlayerList().getPlayers();
        if (!players.isEmpty()) {
            long currentGameTime = level.getGameTime();
            if (currentGameTime == lastWarningMessageTime) {
                return;
            }
            Component modName = Component.translatable("message.notjustspoiled.warning_message.mod_name").withStyle(ChatFormatting.BOLD);
            Component issuesPage = Component.translatable("message.notjustspoiled.warning_message.issues_page").withStyle(Style.EMPTY.withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/TheRealVoin/NotJustSpoiled/issues")));
            players.get(0).sendSystemMessage(Component.translatable("message.notjustspoiled.warning_message", modName, issuesPage));
            lastWarningMessageTime = currentGameTime;
        }
    }
}