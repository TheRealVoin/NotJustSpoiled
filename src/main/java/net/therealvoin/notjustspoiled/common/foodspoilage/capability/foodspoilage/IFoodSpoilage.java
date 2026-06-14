package net.therealvoin.notjustspoiled.common.foodspoilage.capability.foodspoilage;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;

@AutoRegisterCapability
public interface IFoodSpoilage {
    long getLastUpdateTime();
    void setLastUpdateTime(long lastUpdateTime);
    double getFoodLifetime();
    void setFoodLifetime(double foodLifetime);
    FoodEnvironment getEnvironment();
    void setEnvironment(FoodEnvironment foodEnvironment);
}