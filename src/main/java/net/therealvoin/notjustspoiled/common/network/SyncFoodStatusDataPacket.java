package net.therealvoin.notjustspoiled.common.network;

import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.therealvoin.notjustspoiled.client.util.ClientFoodStatusData;
import net.therealvoin.notjustspoiled.common.data.foodstatus.FoodStatusData;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;

import java.util.Map;
import java.util.function.Supplier;

public class SyncFoodStatusDataPacket {
    private final Map<FoodStatus, FoodStatusData> dataMap;

    public SyncFoodStatusDataPacket(Map<FoodStatus, FoodStatusData> dataMap) {
        this.dataMap = dataMap;
    }

    public static void write(SyncFoodStatusDataPacket packet, FriendlyByteBuf buf) {
        buf.writeMap(
                packet.dataMap,
                FriendlyByteBuf::writeEnum,
                (buffer, data) -> buffer.writeJsonWithCodec(FoodStatusData.CODEC, data)
        );
    }

    public static SyncFoodStatusDataPacket read(FriendlyByteBuf buf) {
        return new SyncFoodStatusDataPacket(
                buf.readMap(
                        buffer -> buffer.readEnum(FoodStatus.class),
                        buffer -> buffer.readJsonWithCodec(FoodStatusData.CODEC)
                )
        );
    }

    public static void handle(SyncFoodStatusDataPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            Connection connection = context.getNetworkManager();
            if (connection != null) {
                connection.channel().attr(ClientFoodStatusData.DATA_KEY).set(packet.dataMap);
            }
        });

        context.setPacketHandled(true);
    }
}