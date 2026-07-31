package net.therealvoin.notjustspoiled.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.therealvoin.notjustspoiled.NotJustSpoiled;

public class NJSNetwork {
    private static final String PROTOCOL_VERSION = "1";
    private static int packetId = 0;
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(NotJustSpoiled.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        CHANNEL.registerMessage(
                packetId++,
                DebugMessagePacket.class,
                DebugMessagePacket::write,
                DebugMessagePacket::read,
                DebugMessagePacket::handle
        );

        CHANNEL.registerMessage(
                packetId++,
                SyncFoodStatusDataPacket.class,
                SyncFoodStatusDataPacket::write,
                SyncFoodStatusDataPacket::read,
                SyncFoodStatusDataPacket::handle
        );

        CHANNEL.registerMessage(
                packetId++,
                SyncFoodCategoryDataPacket.class,
                SyncFoodCategoryDataPacket::write,
                SyncFoodCategoryDataPacket::read,
                SyncFoodCategoryDataPacket::handle
        );
    }
}