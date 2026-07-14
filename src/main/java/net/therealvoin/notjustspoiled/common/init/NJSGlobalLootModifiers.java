package net.therealvoin.notjustspoiled.common.init;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.common.data.loot.SetFreshOrStaleStatus;
import net.therealvoin.notjustspoiled.common.data.loot.SetFreshStatus;
import net.therealvoin.notjustspoiled.common.data.loot.SetRandomStatus;
import net.therealvoin.notjustspoiled.common.data.loot.SetSpoiledStatus;

public class NJSGlobalLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS;

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> SET_FRESH_STATUS;
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> SET_FRESH_OR_STALE_STATUS;
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> SET_SPOILED_STATUS;
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> SET_RANDOM_STATUS;

    static {
        GLOBAL_LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, NotJustSpoiled.MOD_ID);
        SET_FRESH_STATUS = GLOBAL_LOOT_MODIFIERS.register("set_fresh_status", () -> SetFreshStatus.CODEC);
        SET_FRESH_OR_STALE_STATUS = GLOBAL_LOOT_MODIFIERS.register("set_fresh_or_stale_status", () -> SetFreshOrStaleStatus.CODEC);
        SET_SPOILED_STATUS = GLOBAL_LOOT_MODIFIERS.register("set_spoiled_status", () -> SetSpoiledStatus.CODEC);
        SET_RANDOM_STATUS = GLOBAL_LOOT_MODIFIERS.register("set_random_status", () -> SetRandomStatus.CODEC);
    }
}