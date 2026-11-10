package net.therealvoin.notjustspoiled.data.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.therealvoin.notjustspoiled.registry.NJSLootItemConditionTypes;
import org.jetbrains.annotations.NotNull;

public class IsArchaeologyCondition implements LootItemCondition {
    public static final IsArchaeologyCondition INSTANCE = new IsArchaeologyCondition();

    private IsArchaeologyCondition() {}

    @Override
    public @NotNull LootItemConditionType getType() {
        return NJSLootItemConditionTypes.IS_ARCHAEOLOGY.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        return lootContext.getResolver().getLootTable(lootContext.getQueriedLootTableId()).getParamSet() == LootContextParamSets.ARCHAEOLOGY;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<IsArchaeologyCondition> {
        @Override
        public void serialize(@NotNull JsonObject pJson, @NotNull IsArchaeologyCondition pValue, @NotNull JsonSerializationContext pSerializationContext) {

        }

        @Override
        public @NotNull IsArchaeologyCondition deserialize(@NotNull JsonObject pJson, @NotNull JsonDeserializationContext pSerializationContext) {
            return INSTANCE;
        }
    }
}