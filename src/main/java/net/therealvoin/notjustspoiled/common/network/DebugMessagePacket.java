package net.therealvoin.notjustspoiled.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.therealvoin.notjustspoiled.client.config.NJSClientConfig;

import java.util.function.Supplier;

public class DebugMessagePacket {
    private final Component message;

    public DebugMessagePacket(Component message) {
        this.message = message;
    }

    public static void write(DebugMessagePacket packet, FriendlyByteBuf buf) {
        buf.writeComponent(packet.message);
    }

    public static DebugMessagePacket read(FriendlyByteBuf buf) {
        return new DebugMessagePacket(buf.readComponent());
    }

    public static void handle(DebugMessagePacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;

            if (player != null && NJSClientConfig.SEND_DEBUG_MESSAGE.get()) {
                player.sendSystemMessage(packet.message);
            }
        });

        context.setPacketHandled(true);
    }

    public static void sendToAll(Component message) {
        NJSNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new DebugMessagePacket(message));
    }
}