package net.therealvoin.notjustspoiled.common.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

public abstract class AbstractSetFoodStatus extends LootModifier {
    private final EnumMap<FoodStatus, DoubleSupplier> chances;

    @SafeVarargs
    protected AbstractSetFoodStatus(LootItemCondition[] conditionsIn, Map.Entry<FoodStatus, DoubleSupplier>... chances) {
        super(conditionsIn);
        this.chances = new EnumMap<>(Map.ofEntries(chances));
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (ItemStack itemStack : generatedLoot) {
            FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);

            if (foodSpoilage != null) {
                foodSpoilage.setEnvironment(FoodEnvironment.STORAGE);
                foodSpoilage.setFoodLifetime(this.getRandomFoodLifetime(FoodCategory.of(itemStack), context.getRandom()));
                foodSpoilage.setLastUpdateTime(context.getLevel().getGameTime());
            }
        }

        return generatedLoot;
    }

    private double getRandomFoodLifetime(FoodCategory foodCategory, RandomSource random) {
        FoodStatus foodStatus = this.getRandomFoodStatus(random);
        int spoilageTime = foodCategory.getSpoilageTime();

        if (foodStatus == FoodStatus.SPOILED) {
            return spoilageTime;
        }

        return random.nextInt(
                (int) foodStatus.getStart(spoilageTime),
                (int) foodStatus.getEnd(spoilageTime)
        );
    }

    private FoodStatus getRandomFoodStatus(RandomSource random) {
        double randomDouble = random.nextDouble();
        double accumulatedChance = 0;

        for (Map.Entry<FoodStatus, DoubleSupplier> entry : chances.entrySet()) {
            accumulatedChance += entry.getValue().getAsDouble();
            if (randomDouble < accumulatedChance) {
                return entry.getKey();
            }
        }

        return null;
    }

    protected static <T extends AbstractSetFoodStatus> Codec<T> createCodec(Function<LootItemCondition[], T> constructor) {
        return RecordCodecBuilder.create(
                instance -> codecStart(instance).apply(
                        instance,
                        constructor
                )
        );
    }
}