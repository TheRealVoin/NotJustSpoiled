package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.therealvoin.notjustspoiled.client.util.ClientFoodStatusData;
import net.therealvoin.notjustspoiled.common.data.foodstatus.FoodStatusData;
import net.therealvoin.notjustspoiled.common.data.foodstatus.FoodStatusEffectData;

public enum FoodStatus {
    FRESH("fresh", ChatFormatting.GREEN, 0),
    STALE("stale", ChatFormatting.YELLOW, 1.0 / 3.0),
    HALF_SPOILED("half-spoiled", ChatFormatting.GOLD, 2.0 / 3.0),
    SPOILED("spoiled", ChatFormatting.RED, 1);

    private final String key;
    private final ChatFormatting color;
    private final double start;
    private FoodStatusData serverData;

    FoodStatus(String key, ChatFormatting color, double start) {
        this.key = key;
        this.color = color;
        this.start = start;
    }

    public Component getDisplayName() {
        return Component.translatable("tooltip.notjustspoiled.food_status." + this.key).withStyle(this.color);
    }

    public ChatFormatting getColor() {
        return this.color;
    }

    public FoodStatus getNext() {
        return this.ordinal() < values().length - 1 ? values()[this.ordinal() + 1] : null;
    }

    public double getStart(int spoilageTime) {
        return spoilageTime * this.start;
    }

    public double getEnd(int spoilageTime) {
        FoodStatus nextFoodStatus = this.getNext();
        return nextFoodStatus == null ? spoilageTime : nextFoodStatus.getStart(spoilageTime) - 1;
    }

    public int getModifiedNutrition(int defaultNutrition) {
        return defaultNutrition == 0 ? defaultNutrition : Math.max(1, (int) Math.round(defaultNutrition * this.getData().nutritionMultiplier()));
    }

    public FoodStatusData getData() {
        if (EffectiveSide.get().isServer()) {
            return this.serverData;
        } else {
            return ClientFoodStatusData.getData().get(this);
        }
    }

    public FoodStatusData getServerData() {
        return this.serverData;
    }

    public void setServerData(FoodStatusData data) {
        this.serverData = data;
    }

    public void applyEffects(LivingEntity entity) {
        for (FoodStatusEffectData effectData : this.getServerData().effects()) {
            if (entity.getRandom().nextFloat() < effectData.applyChance()) {
                entity.addEffect(
                        new MobEffectInstance(
                                effectData.effect(),
                                effectData.duration(),
                                effectData.amplifier(),
                                false,
                                false
                        ),
                        entity
                );
            }
        }
    }

    public static FoodStatus byName(String name) {
        for (FoodStatus foodStatus : values()) {
            if (name.equals(foodStatus.key)) {
                return foodStatus;
            }
        }

        return null;
    }
}