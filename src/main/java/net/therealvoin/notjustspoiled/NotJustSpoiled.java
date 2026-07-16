package net.therealvoin.notjustspoiled;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.therealvoin.notjustspoiled.client.config.NJSClientConfig;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;
import net.therealvoin.notjustspoiled.common.init.NJSGlobalLootModifiers;
import net.therealvoin.notjustspoiled.common.network.NJSNetwork;
import net.therealvoin.notjustspoiled.compat.appleskin.AppleSkinCompat;
import org.slf4j.Logger;

@Mod(NotJustSpoiled.MOD_ID)
public class NotJustSpoiled {
    public static final String MOD_NAME = "Not Just Spoiled";
    public static final String MOD_ID = "notjustspoiled";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NotJustSpoiled(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        NJSNetwork.register();
        NJSGlobalLootModifiers.GLOBAL_LOOT_MODIFIERS.register(modEventBus);
        context.registerConfig(ModConfig.Type.CLIENT, NJSClientConfig.CONFIG);
        context.registerConfig(ModConfig.Type.SERVER, NJSServerConfig.CONFIG);

        AppleSkinCompat.init();
    }
}