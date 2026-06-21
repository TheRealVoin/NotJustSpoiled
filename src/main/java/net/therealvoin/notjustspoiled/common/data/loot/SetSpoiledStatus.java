package net.therealvoin.notjustspoiled.common.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.foodspoilage.FoodSpoilageProvider;
import org.jetbrains.annotations.NotNull;

public class SetSpoiledStatus extends LootModifier {
    public static final Codec<SetSpoiledStatus> CODEC =
            RecordCodecBuilder.create(instance -> codecStart(instance).apply(instance, SetSpoiledStatus::new));

    public SetSpoiledStatus(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (ItemStack itemStack : generatedLoot) {
            itemStack.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage -> {
                foodSpoilage.setEnvironment(FoodEnvironment.STORAGE);
                foodSpoilage.setFoodLifetime(FoodCategory.getFoodCategory(itemStack).getSpoilageTime());
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