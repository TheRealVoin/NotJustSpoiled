package net.therealvoin.notjustspoiled;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.therealvoin.notjustspoiled.config.NJSClientConfig;
import net.therealvoin.notjustspoiled.config.NJSServerConfig;
import net.therealvoin.notjustspoiled.registry.NJSGlobalLootModifiers;
import net.therealvoin.notjustspoiled.network.NJSNetwork;
import net.therealvoin.notjustspoiled.compat.appleskin.AppleSkinIntegration;
import net.therealvoin.notjustspoiled.registry.NJSLootItemConditionTypes;
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
        NJSLootItemConditionTypes.LOOT_ITEM_CONDITION_TYPES.register(modEventBus);
        context.registerConfig(ModConfig.Type.CLIENT, NJSClientConfig.CONFIG);
        context.registerConfig(ModConfig.Type.SERVER, NJSServerConfig.CONFIG);

        AppleSkinIntegration.init();
    }
}