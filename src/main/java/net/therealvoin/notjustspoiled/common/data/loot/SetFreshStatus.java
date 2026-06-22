package net.therealvoin.notjustspoiled.common.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.FoodSpoilageProvider;
import org.jetbrains.annotations.NotNull;

public class SetFreshStatus extends LootModifier {
    public static final Codec<SetFreshStatus> CODEC =
            RecordCodecBuilder.create(instance -> codecStart(instance).apply(instance, SetFreshStatus::new));

    public SetFreshStatus(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (ItemStack itemStack : generatedLoot) {
            itemStack.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
                foodSpoilage.setEnvironment(FoodEnvironment.STORAGE);
                // 1 instead of 0 to prevent the warning message from appearing
                foodSpoilage.setFoodLifetime(1);
                foodSpoilage.setLastUpdateTime(context.getLevel().getGameTime());
            });
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}