package net.therealvoin.notjustspoiled.client.util;

import io.netty.util.AttributeKey;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.common.data.foodstatus.FoodStatusData;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;

import java.util.Map;

public class ClientFoodStatusData {
    public static final AttributeKey<Map<FoodStatus, FoodStatusData>> DATA_KEY = AttributeKey.valueOf(NotJustSpoiled.MOD_ID + ":client_food_status_data");

    public static Map<FoodStatus, FoodStatusData> getData() {
        Connection connection = Minecraft.getInstance().getConnection().getConnection();
        if (connection.channel().hasAttr(DATA_KEY)) {
            return connection.channel().attr(DATA_KEY).get();
        }

        return Map.of();
    }
}