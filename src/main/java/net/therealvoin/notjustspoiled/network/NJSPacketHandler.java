package net.therealvoin.notjustspoiled.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.therealvoin.notjustspoiled.NotJustSpoiled;

public class NJSPacketHandler {
    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(NotJustSpoiled.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    public static void register() {
        int id = 0;

        CHANNEL.registerMessage(
                id++,
                MergeFoodPacket.class,
                MergeFoodPacket::encode,
                MergeFoodPacket::decode,
                MergeFoodPacket::handle
        );
    }
}