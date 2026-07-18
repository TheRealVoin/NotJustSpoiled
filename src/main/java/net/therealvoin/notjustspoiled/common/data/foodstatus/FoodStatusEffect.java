package net.therealvoin.notjustspoiled.common.data.foodstatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

public record FoodStatusEffect(MobEffect effect, int duration, int amplifier, float applyChance) {
    public static final Codec<FoodStatusEffect> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ForgeRegistries.MOB_EFFECTS.getCodec()
                            .fieldOf("effect")
                            .forGetter(FoodStatusEffect::effect),
                    Codec.intRange(1, Integer.MAX_VALUE)
                            .fieldOf("duration")
                            .forGetter(FoodStatusEffect::duration),
                    Codec.intRange(0, 255)
                            .fieldOf("amplifier")
                            .forGetter(FoodStatusEffect::amplifier),
                    Codec.floatRange(0, 1)
                            .fieldOf("chance")
                            .forGetter(FoodStatusEffect::applyChance)
            ).apply(instance, FoodStatusEffect::new)
    );
}