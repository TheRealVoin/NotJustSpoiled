package net.therealvoin.notjustspoiled.integration;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodStatus;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

public class AppleSkinIntegration {
    public static void init() {
        if (ModList.get().isLoaded("appleskin")) {
            MinecraftForge.EVENT_BUS.register(AppleSkinIntegration.class);
        }
    }

    @SubscribeEvent
    public static void onFoodValues(FoodValuesEvent event) {
        FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(event.itemStack, event.player.level());
        if (foodStatus == null) {
            return;
        }

        int defaultNutrition = event.defaultFoodValues.hunger;
        float defaultSaturationModifier = event.defaultFoodValues.saturationModifier;
        event.modifiedFoodValues = new FoodValues(foodStatus.getModifiedNutrition(defaultNutrition), foodStatus.getModifiedSaturation(defaultSaturationModifier));
    }
}