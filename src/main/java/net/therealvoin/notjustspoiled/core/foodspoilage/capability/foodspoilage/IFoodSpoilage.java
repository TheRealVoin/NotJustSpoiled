package net.therealvoin.notjustspoiled.core.foodspoilage.capability.foodspoilage;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;

@AutoRegisterCapability
public interface IFoodSpoilage {
    long getLastUpdateTime();
    void setLastUpdateTime(long lastUpdateTime);
    double getFoodLifetime();
    void addFoodLifetime(double foodLifetimeToAdd);
    void setFoodLifetime(double foodLifetime);
    FoodEnvironment getEnvironment();
    void setEnvironment(FoodEnvironment foodEnvironment);
}