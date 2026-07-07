package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class FoodSpoilage {
    private final ItemStack itemStack;

    private FoodSpoilage(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public long getLastUpdateTime() {
        return this.getFoodSpoilageTag().contains("lastUpdateTime") ? this.getFoodSpoilageTag().getLong("lastUpdateTime") : -1;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.getFoodSpoilageTag().putLong("lastUpdateTime", lastUpdateTime);
    }

    public double getFoodLifetime() {
        return this.getFoodSpoilageTag().contains("foodLifetime") ? this.getFoodSpoilageTag().getDouble("foodLifetime") : 0;
    }

    public void setFoodLifetime(double foodLifetime) {
        this.getFoodSpoilageTag().putDouble("foodLifetime", foodLifetime);
    }

    public FoodEnvironment getEnvironment() {
        return this.getFoodSpoilageTag().contains("environment") ? FoodEnvironment.valueOf(this.getFoodSpoilageTag().getString("environment")) : null;
    }

    public void setEnvironment(FoodEnvironment foodEnvironment) {
        this.getFoodSpoilageTag().putString("environment", foodEnvironment.name());
    }

    private CompoundTag getFoodSpoilageTag() {
        return this.itemStack.getOrCreateTagElement("food_spoilage");
    }

    public static FoodSpoilage of(ItemStack itemStack) {
        if (FoodCategory.getFoodCategory(itemStack) == null) {
            return null;
        }

        return new FoodSpoilage(itemStack);
    }
}