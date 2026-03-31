package net.therealvoin.notjustspoiled.util.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.core.foodspoilage.capability.foodspoilage.FoodSpoilageProvider;
import org.jetbrains.annotations.NotNull;

public class SetRandomStatus extends LootModifier {
    public static final Codec<SetRandomStatus> CODEC =
            RecordCodecBuilder.create(instance -> codecStart(instance).apply(instance, SetRandomStatus::new));

    protected SetRandomStatus(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (ItemStack itemStack : generatedLoot) {
            itemStack.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
                foodSpoilage.setEnvironment(FoodEnvironment.STORAGE);
                foodSpoilage.setFoodLifetime(FoodSpoilageManager.getRandomFoodLifetime(FoodCategory.getFoodCategory(itemStack), context.getRandom()));
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