package net.therealvoin.notjustspoiled.common.data.foodcategory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FoodCategoryReloadListener extends SimpleJsonResourceReloadListener {
    public FoodCategoryReloadListener() {
        super(new Gson(), "food_category");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> jsons, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            String jsonFoodCategory = entry.getKey().getPath();
            FoodCategory foodCategory = FoodCategory.byName(jsonFoodCategory);

            if (foodCategory == null) {
                NotJustSpoiled.LOGGER.error("Unknown food category found: {}", jsonFoodCategory);
                continue;
            }

            DataResult<FoodCategoryData> result = FoodCategoryData.CODEC.parse(JsonOps.INSTANCE, entry.getValue());

            result.error().ifPresent(error -> NotJustSpoiled.LOGGER.error("{}: {}", entry.getKey(), error.message()));

            result.result().ifPresent(foodCategory::setServerData);
        }
    }
}