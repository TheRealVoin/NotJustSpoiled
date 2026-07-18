package net.therealvoin.notjustspoiled.common.data.loot;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;

import java.util.Map;

public class SetSpoiledStatus extends AbstractSetFoodStatus {
    public static final Codec<SetSpoiledStatus> CODEC = createCodec(SetSpoiledStatus::new);

    public SetSpoiledStatus(LootItemCondition[] conditionsIn) {
        super(
                conditionsIn,
                Map.entry(FoodStatus.SPOILED, () -> 1)
        );
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}