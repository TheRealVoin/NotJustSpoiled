package net.therealvoin.notjustspoiled.common.data.loot;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;

import java.util.Map;

public class SetFreshStatus extends AbstractSetFoodStatus {
    public static final Codec<SetFreshStatus> CODEC = createCodec(SetFreshStatus::new);

    public SetFreshStatus(LootItemCondition[] conditionsIn) {
        super(
                conditionsIn,
                Map.entry(FoodStatus.FRESH, () -> 1)
        );
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}