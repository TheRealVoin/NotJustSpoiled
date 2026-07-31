package net.therealvoin.notjustspoiled.common.network;

import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.therealvoin.notjustspoiled.client.util.ClientFoodCategoryData;
import net.therealvoin.notjustspoiled.common.data.foodcategory.FoodCategoryData;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodCategory;

import java.util.Map;
import java.util.function.Supplier;

public class SyncFoodCategoryDataPacket {
    private final Map<FoodCategory, FoodCategoryData> dataMap;

    public SyncFoodCategoryDataPacket(Map<FoodCategory, FoodCategoryData> dataMap) {
        this.dataMap = dataMap;
    }

    public static void write(SyncFoodCategoryDataPacket packet, FriendlyByteBuf buf) {
        buf.writeMap(
                packet.dataMap,
                FriendlyByteBuf::writeEnum,
                (buffer, data) -> buffer.writeJsonWithCodec(FoodCategoryData.CODEC, data)
        );
    }

    public static SyncFoodCategoryDataPacket read(FriendlyByteBuf buf) {
        return new SyncFoodCategoryDataPacket(
                buf.readMap(
                        buffer -> buffer.readEnum(FoodCategory.class),
                        buffer -> buffer.readJsonWithCodec(FoodCategoryData.CODEC)
                )
        );
    }

    public static void handle(SyncFoodCategoryDataPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            Connection connection = context.getNetworkManager();
            if (connection != null) {
                connection.channel().attr(ClientFoodCategoryData.DATA_KEY).set(packet.dataMap);
            }
        });

        context.setPacketHandled(true);
    }
}