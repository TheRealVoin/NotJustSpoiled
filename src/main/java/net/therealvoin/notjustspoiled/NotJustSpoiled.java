package net.therealvoin.notjustspoiled;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.therealvoin.notjustspoiled.core.config.NJSConfig;
import net.therealvoin.notjustspoiled.init.NJSGlobalLootModifiers;

@Mod(NotJustSpoiled.MOD_ID)
public class NotJustSpoiled {
    public static final String MOD_ID = "notjustspoiled";

    public NotJustSpoiled(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        NJSGlobalLootModifiers.GLOBAL_LOOT_MODIFIERS_DEFERRED_REGISTER.register(modEventBus);
        context.registerConfig(ModConfig.Type.SERVER, NJSConfig.CONFIG);
    }
}