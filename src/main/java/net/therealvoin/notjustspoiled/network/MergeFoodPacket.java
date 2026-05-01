package net.therealvoin.notjustspoiled.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;

import java.util.function.Supplier;

public class MergeFoodPacket {
    private final ItemStack itemStack1;
    private final ItemStack itemStack2;

    public MergeFoodPacket(ItemStack itemStack1, ItemStack itemStack2) {
        this.itemStack1 = itemStack1;
        this.itemStack2 = itemStack2;
    }

    public static void encode(MergeFoodPacket message, FriendlyByteBuf buf) {
        buf.writeItem(message.itemStack1);
        buf.writeItem(message.itemStack2);
    }

    public static MergeFoodPacket decode(FriendlyByteBuf buf) {
        return new MergeFoodPacket(
                buf.readItem(),
                buf.readItem()
        );
    }

    public static void handle(MergeFoodPacket message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();

            if (player == null) {
                return;
            }

            if (!FoodSpoilageManager.canMergeFood(message.itemStack1, message.itemStack2, player.serverLevel())) {
                return;
            }

            FoodSpoilageManager.changeEnvironmentAndUpdate(message.itemStack2, FoodEnvironment.INVENTORY, player.serverLevel());
            FoodSpoilageManager.tryAverageSpoilageOnMerge(message.itemStack1, message.itemStack2, player.serverLevel());
        });

        context.get().setPacketHandled(true);
        System.out.println("Handled!!!!");
    }
}