package net.therealvoin.notjustspoiled.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.therealvoin.notjustspoiled.data.NJSTags;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodCategory;

import java.util.List;

public class FoodFinderUtil {
    public static List<ResourceLocation> getAllEdibleFoodIds(String modId) {
        return ForgeRegistries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(modId))
                .filter(resourceKeyItemEntry -> resourceKeyItemEntry.getValue().isEdible())
                .map(entry -> entry.getKey().location())
                .toList();
    }

    public static List<ResourceLocation> getAllEdibleFoodIdsWithTag(String modId) {
        return ForgeRegistries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(modId))
                .filter(resourceKeyItemEntry -> resourceKeyItemEntry.getValue().isEdible())
                .filter(resourceKeyItemEntry -> FoodCategory.getFoodCategory(new ItemStack(resourceKeyItemEntry.getValue())) != null || new ItemStack(resourceKeyItemEntry.getValue()).is(NJSTags.Items.ALWAYS_SPOILED) || new ItemStack(resourceKeyItemEntry.getValue()).is(NJSTags.Items.NEVER_SPOILS))
                .map(entry -> entry.getKey().location())
                .toList();
    }
}