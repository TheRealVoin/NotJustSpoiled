package net.therealvoin.notjustspoiled.common.data.loot;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;
import net.therealvoin.notjustspoiled.common.foodspoilage.*;

import java.util.Map;

public class SetFreshOrStaleStatus extends AbstractSetFoodStatus {
    public static final Codec<SetFreshOrStaleStatus> CODEC = createCodec(SetFreshOrStaleStatus::new);

    public SetFreshOrStaleStatus(LootItemCondition[] conditionsIn) {
        super(
                conditionsIn,
                Map.entry(FoodStatus.FRESH, NJSServerConfig.FRESH_OR_STALE$CHANCE_TO_APPEAR_FRESH_FOOD_IN_STORAGE::get),
                Map.entry(FoodStatus.STALE, NJSServerConfig.FRESH_OR_STALE$CHANCE_TO_APPEAR_STALE_FOOD_IN_STORAGE::get)
        );
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}