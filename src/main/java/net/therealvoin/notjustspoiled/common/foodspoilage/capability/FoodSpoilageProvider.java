package net.therealvoin.notjustspoiled.common.foodspoilage.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FoodSpoilageProvider implements IFoodSpoilage, ICapabilitySerializable<CompoundTag> {
    public static final Capability<IFoodSpoilage> FOOD_SPOILAGE = CapabilityManager.get(new CapabilityToken<>() {});
    private final LazyOptional<IFoodSpoilage> optional = LazyOptional.of(() -> this);

    private long lastUpdateTime = 0;
    private double foodLifetime = 0;
    private FoodEnvironment foodEnvironment = FoodEnvironment.NONE;

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == FOOD_SPOILAGE) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    @Override
    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public double getFoodLifetime() {
        return this.foodLifetime;
    }

    @Override
    public void setFoodLifetime(double foodLifetime) {
        this.foodLifetime = foodLifetime;
    }

    @Override
    public FoodEnvironment getEnvironment() {
        return this.foodEnvironment;
    }

    @Override
    public void setEnvironment(FoodEnvironment foodEnvironment) {
        this.foodEnvironment = foodEnvironment;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbtTag = new CompoundTag();
        nbtTag.putLong("lastUpdateTime", this.getLastUpdateTime());
        nbtTag.putDouble("foodLifetime", this.getFoodLifetime());
        nbtTag.putString("environment", this.getEnvironment().name());
        return nbtTag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        if (compoundTag != null && compoundTag.contains("foodLifetime") && compoundTag.contains("lastUpdateTime") && compoundTag.contains("environment")) {
            this.lastUpdateTime = compoundTag.getLong("lastUpdateTime");
            this.foodLifetime = compoundTag.getDouble("foodLifetime");
            this.foodEnvironment = FoodEnvironment.valueOf(compoundTag.getString("environment"));
        }
    }
}