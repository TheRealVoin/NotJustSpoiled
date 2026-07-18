package net.therealvoin.notjustspoiled.common.data.foodstatus;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class FoodStatusReloadListener extends SimpleJsonResourceReloadListener {
    private static final Map<FoodStatus, FoodStatusData> DATA = new EnumMap<>(FoodStatus.class);

    public FoodStatusReloadListener() {
        super(new Gson(), "food_status");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> jsons, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            String jsonFoodStatus = entry.getKey().getPath();
            FoodStatus foodStatus = FoodStatus.byName(jsonFoodStatus);

            if (foodStatus == null) {
                NotJustSpoiled.LOGGER.error("Unknown food status found: {}", jsonFoodStatus);
                continue;
            }

            DataResult<FoodStatusData> result = FoodStatusData.CODEC.parse(JsonOps.INSTANCE, entry.getValue());
            result.error().ifPresent(error -> NotJustSpoiled.LOGGER.error("{}: {}", entry.getKey(), error.message()));
            result.result().ifPresent(data -> DATA.put(foodStatus, data));
        }
    }

    public static FoodStatusData get(FoodStatus foodStatus) {
        return DATA.get(foodStatus);
    }
}