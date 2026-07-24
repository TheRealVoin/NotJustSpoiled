package net.therealvoin.notjustspoiled.common.data.foodstatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

public record FoodStatusEffectData(MobEffect effect, int duration, int amplifier, double applyChance) {
    public static final Codec<FoodStatusEffectData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ForgeRegistries.MOB_EFFECTS.getCodec()
                            .fieldOf("effect")
                            .forGetter(FoodStatusEffectData::effect),
                    Codec.intRange(1, Integer.MAX_VALUE)
                            .fieldOf("duration")
                            .forGetter(FoodStatusEffectData::duration),
                    Codec.intRange(0, 255)
                            .fieldOf("amplifier")
                            .forGetter(FoodStatusEffectData::amplifier),
                    Codec.doubleRange(0, 1)
                            .fieldOf("chance")
                            .forGetter(FoodStatusEffectData::applyChance)
            ).apply(instance, FoodStatusEffectData::new)
    );
}