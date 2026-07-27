package net.therealvoin.notjustspoiled.common.util;

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
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.therealvoin.notjustspoiled.client.util.NJSClientUtils;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

public class NJSUtils {
    public static void removeSpoilageTagFromEqualityCheck(Args args, Level level) {
        ItemStack itemStack1 = args.get(0);
        ItemStack itemStack2 = args.get(1);
        FoodSpoilage foodSpoilage1 = FoodSpoilage.of(itemStack1);
        FoodSpoilage foodSpoilage2 = FoodSpoilage.of(itemStack2);

        if (foodSpoilage1 == null || !foodSpoilage1.isInitialized() || foodSpoilage2 == null || !foodSpoilage2.isInitialized()) {
            return;
        }

        FoodStatus foodStatus1 = FoodSpoilageManager.getFoodStatus(itemStack1, level);
        FoodStatus foodStatus2 = FoodSpoilageManager.getFoodStatus(itemStack2, level);

        if (foodStatus1 != foodStatus2) {
            return;
        }

        ItemStack stackWithoutSpoilageTag1 = itemStack1.copy();
        ItemStack stackWithoutSpoilageTag2 = itemStack2.copy();
        stackWithoutSpoilageTag1.getTag().remove("food_spoilage");
        stackWithoutSpoilageTag2.getTag().remove("food_spoilage");

        args.set(0, stackWithoutSpoilageTag1);
        args.set(1, stackWithoutSpoilageTag2);
    }

    public static Level getLevelWithoutContext() {
        if (EffectiveSide.get().isServer()) {
            return ServerLifecycleHooks.getCurrentServer().overworld();
        } else {
            return NJSClientUtils.getClientLevel();
        }
    }

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

        return getLevelWithoutContext();
    }
}