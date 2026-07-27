package net.therealvoin.notjustspoiled.client.util;

import io.netty.util.AttributeKey;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.common.data.foodcategory.FoodCategoryData;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodCategory;

import java.util.Map;

public class ClientFoodCategoryData {
    public static final AttributeKey<Map<FoodCategory, FoodCategoryData>> DATA_KEY = AttributeKey.valueOf(NotJustSpoiled.MOD_ID + ":client_food_category_data");

    public static Map<FoodCategory, FoodCategoryData> getData() {
        Connection connection = Minecraft.getInstance().getConnection().getConnection();
        if (connection.channel().hasAttr(DATA_KEY)) {
            return connection.channel().attr(DATA_KEY).get();
        }

        return Map.of();
    }
}