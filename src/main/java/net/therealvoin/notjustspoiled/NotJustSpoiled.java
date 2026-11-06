package net.therealvoin.notjustspoiled;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(NotJustSpoiled.MODID)
public class NotJustSpoiled {
    public static final String MODID = "notjustspoiled";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NotJustSpoiled(IEventBus modEventBus, ModContainer modContainer) {
    }
}
