package net.therealvoin.notjustspoiled.common.data.foodcategory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FoodCategoryData(int spoilageTime) {
    public static final Codec<FoodCategoryData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.intRange(0, Integer.MAX_VALUE)
                            .fieldOf("spoilage_time")
                            .forGetter(FoodCategoryData::spoilageTime)
            ).apply(instance, FoodCategoryData::new)
    );
}