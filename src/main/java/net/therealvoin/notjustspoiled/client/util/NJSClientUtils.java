package net.therealvoin.notjustspoiled.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class NJSClientUtils {
    public static Level getClientLevel() {
        return Minecraft.getInstance().level;
    }
}