package net.therealvoin.notjustspoiled.common.data.foodstatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record FoodStatusData(double nutritionMultiplier, List<FoodStatusEffectData> effects) {
    public static final Codec<FoodStatusData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.doubleRange(0, 1)
                            .fieldOf("nutritionMultiplier")
                            .forGetter(FoodStatusData::nutritionMultiplier),
                    FoodStatusEffectData.CODEC
                            .listOf()
                            .optionalFieldOf("effects", List.of())
                            .forGetter(FoodStatusData::effects)
            ).apply(instance, FoodStatusData::new)
    );
}