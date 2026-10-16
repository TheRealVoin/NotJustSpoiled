package net.therealvoin.notjustspoiled.common.data.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.jetbrains.annotations.NotNull;

public class SpoiledFoodBrewingRecipe implements IBrewingRecipe {
    @Override
    public boolean isInput(@NotNull ItemStack input) {
        return PotionUtils.getPotion(input) == Potions.AWKWARD;
    }

    @Override
    public boolean isIngredient(@NotNull ItemStack ingredient) {
        return FoodSpoilage.of(ingredient) != null;
    }

    @Override
    public @NotNull ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack ingredient) {
        if (!this.isInput(input) || !this.isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }

        if (FoodSpoilageManager.getFoodStatus(ingredient, NJSUtils.getLevelWithoutContext()) == FoodStatus.SPOILED) {
            return PotionUtils.setPotion(input.copy(), Potions.POISON);
        }

        return ItemStack.EMPTY;
    }
}