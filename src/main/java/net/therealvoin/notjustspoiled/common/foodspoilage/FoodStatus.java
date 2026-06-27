package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.ForgeConfigSpec;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;

import java.util.List;

public enum FoodStatus {
    FRESH("fresh", ChatFormatting.GREEN, NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE),
    STALE("stale", ChatFormatting.YELLOW, NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE),
    HALF_SPOILED("half-spoiled", ChatFormatting.GOLD, NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_HALF_SPOILED_FOOD_IN_STORAGE,
            new EffectData(MobEffects.POISON,
                    NJSServerConfig.POISON_EFFECT_FOR_HALF_SPOILED_FOOD_DURATION,
                    0,
                    NJSServerConfig.CHANCE_TO_APPLY_POISON_EFFECT_FOR_HALF_SPOILED_FOOD
            ),
            new EffectData(MobEffects.HUNGER,
                    NJSServerConfig.HUNGER_EFFECT_FOR_HALF_SPOILED_FOOD_DURATION,
                    0,
                    NJSServerConfig.CHANCE_TO_APPLY_HUNGER_EFFECT_FOR_HALF_SPOILED_FOOD
            ),
            new EffectData(MobEffects.CONFUSION,
                    NJSServerConfig.NAUSEA_EFFECT_FOR_HALF_SPOILED_FOOD_DURATION,
                    0,
                    NJSServerConfig.CHANCE_TO_APPLY_NAUSEA_EFFECT_FOR_HALF_SPOILED_FOOD
            )
    ),
    SPOILED("spoiled", ChatFormatting.RED, NJSServerConfig.RANDOM$CHANCE_TO_APPEAR_SPOILED_FOOD_IN_STORAGE,
            new EffectData(
                    MobEffects.POISON,
                    NJSServerConfig.POISON_EFFECT_FOR_SPOILED_FOOD_DURATION,
                    0,
                    NJSServerConfig.CHANCE_TO_APPLY_POISON_EFFECT_FOR_SPOILED_FOOD
            ),
            new EffectData(
                    MobEffects.HUNGER,
                    NJSServerConfig.HUNGER_EFFECT_FOR_SPOILED_FOOD_DURATION,
                    1,
                    NJSServerConfig.CHANCE_TO_APPLY_HUNGER_EFFECT_FOR_SPOILED_FOOD
            ),
            new EffectData(
                    MobEffects.CONFUSION,
                    NJSServerConfig.NAUSEA_EFFECT_FOR_SPOILED_FOOD_DURATION,
                    0,
                    NJSServerConfig.CHANCE_TO_APPLY_NAUSEA_EFFECT_FOR_SPOILED_FOOD
            )
    );

    private final String translationKey;
    private final ChatFormatting color;
    private final ForgeConfigSpec.DoubleValue chanceToAppearInStorage;
    private final List<EffectData> effectsData;

    FoodStatus(String translationKey, ChatFormatting color, ForgeConfigSpec.DoubleValue chanceToAppearInStorage, EffectData... effectsData) {
        this.translationKey = translationKey;
        this.color = color;
        this.chanceToAppearInStorage = chanceToAppearInStorage;
        this.effectsData = List.of(effectsData);
    }

    public Component getTranslation() {
        return Component.translatable("tooltip.notjustspoiled.status." + this.translationKey).withStyle(this.color);
    }

    public ChatFormatting getColor() {
        return this.color;
    }

    public double getChanceToAppearInStorage() {
        return this.chanceToAppearInStorage.get();
    }

    public double getThreshold(int spoilageTime) {
        return switch (this) {
            case FRESH -> spoilageTime / 3.0;
            case STALE -> spoilageTime / 3.0 * 2;
            case HALF_SPOILED -> spoilageTime;
            case SPOILED -> throw new IllegalStateException("Spoiled status has no threshold");
        };
    }

    public FoodStatus getNext() {
        return this.ordinal() < values().length - 1 ? values()[this.ordinal() + 1] : null;
    }

    public int getModifiedNutrition(int defaultNutrition) {
        switch (this) {
            case STALE -> {
                if (defaultNutrition <= 4) {
                    return defaultNutrition;
                } else if (defaultNutrition <= 10) {
                    return defaultNutrition - 1;
                } else if (defaultNutrition <= 14) {
                    return defaultNutrition - 2;
                } else if (defaultNutrition <= 22) {
                    return defaultNutrition - 3;
                } else {
                    return defaultNutrition - 4;
                }
            }

            case HALF_SPOILED -> {
                if (defaultNutrition == 1) {
                    return defaultNutrition;
                } else if (defaultNutrition <= 3) {
                    return defaultNutrition - 1;
                } else if (defaultNutrition <= 6) {
                    return defaultNutrition - 2;
                } else if (defaultNutrition <= 8) {
                    return defaultNutrition - 3;
                } else if (defaultNutrition <= 10) {
                    return defaultNutrition - 4;
                } else if (defaultNutrition <= 12) {
                    return defaultNutrition - 5;
                } else if (defaultNutrition <= 14) {
                    return defaultNutrition - 6;
                } else if (defaultNutrition <= 16) {
                    return defaultNutrition - 7;
                } else if (defaultNutrition <= 18) {
                    return defaultNutrition - 8;
                } else if (defaultNutrition <= 20) {
                    return defaultNutrition - 9;
                } else if (defaultNutrition <= 22) {
                    return defaultNutrition - 10;
                } else {
                    return defaultNutrition - 11;
                }
            }

            case SPOILED -> {
                if (defaultNutrition == 1) {
                    return defaultNutrition;
                } else if (defaultNutrition == 2) {
                    return defaultNutrition - 1;
                } else if (defaultNutrition == 3) {
                    return defaultNutrition - 2;
                } else if (defaultNutrition <= 5) {
                    return defaultNutrition - 3;
                } else if (defaultNutrition <= 7) {
                    return defaultNutrition - 4;
                } else if (defaultNutrition == 8) {
                    return defaultNutrition - 5;
                } else if (defaultNutrition == 9) {
                    return defaultNutrition - 6;
                } else if (defaultNutrition <= 11) {
                    return defaultNutrition - 7;
                } else if (defaultNutrition <= 13) {
                    return defaultNutrition - 8;
                } else if (defaultNutrition <= 15) {
                    return defaultNutrition - 9;
                } else if (defaultNutrition <= 17) {
                    return defaultNutrition - 10;
                } else if (defaultNutrition <= 22) {
                    return defaultNutrition - 12;
                } else {
                    return defaultNutrition - 13;
                }
            }
        }

        return defaultNutrition;
    }

    public float getModifiedSaturation(float defaultSaturation) {
        return defaultSaturation;
    }

    public static FoodStatus getFreshOrStaleStatus(RandomSource random) {
        double randomDouble = random.nextDouble();

        double fresh = NJSServerConfig.FRESH_OR_STALE$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE.get();

        if (randomDouble < fresh) {
            return FRESH;
        } else {
            return STALE;
        }
    }

    public static FoodStatus getRandomFoodStatus(RandomSource random) {
        double randomDouble = random.nextDouble();

        double fresh = FRESH.getChanceToAppearInStorage();
        double stale = STALE.getChanceToAppearInStorage();
        double halfSpoiled = HALF_SPOILED.getChanceToAppearInStorage();

        if (randomDouble < fresh) {
            return FRESH;
        } else if (randomDouble < fresh + stale) {
            return STALE;
        } else if (randomDouble < fresh + stale + halfSpoiled) {
            return HALF_SPOILED;
        } else {
            return SPOILED;
        }
    }

    public List<EffectData> getEffectsData() {
        return this.effectsData;
    }


    public static class EffectData {
        private final MobEffect effect;
        private final ForgeConfigSpec.IntValue duration;
        private final int amplifier;
        private final ForgeConfigSpec.DoubleValue applyChance;


        public EffectData(MobEffect effect, ForgeConfigSpec.IntValue duration, int amplifier, ForgeConfigSpec.DoubleValue applyChance) {
            this.effect = effect;
            this.duration = duration;
            this.amplifier = amplifier;
            this.applyChance = applyChance;
        }

        public MobEffect getEffect() {
            return this.effect;
        }

        public int getDuration() {
            return this.duration.get();
        }

        public int getAmplifier() {
            return this.amplifier;
        }

        public MobEffectInstance createEffectInstance() {
            return createEffectInstance(this.effect);
        }

        // Added specifically for TAN integration
        public MobEffectInstance createEffectInstance(MobEffect effect) {
            return new MobEffectInstance(effect, this.duration.get(), this.amplifier, false, false);
        }

        public double getApplyChance() {
            return this.applyChance.get();
        }
    }
}