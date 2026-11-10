package net.therealvoin.notjustspoiled.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.data.loot.condition.IsArchaeologyCondition;

public class NJSLootItemConditionTypes {
    public static final DeferredRegister<LootItemConditionType> LOOT_ITEM_CONDITION_TYPES;

    public static final RegistryObject<LootItemConditionType> IS_ARCHAEOLOGY;

    static {
        LOOT_ITEM_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, NotJustSpoiled.MOD_ID);

        IS_ARCHAEOLOGY = LOOT_ITEM_CONDITION_TYPES.register("is_archaeology", () -> new LootItemConditionType(new IsArchaeologyCondition.Serializer()));
    }
}