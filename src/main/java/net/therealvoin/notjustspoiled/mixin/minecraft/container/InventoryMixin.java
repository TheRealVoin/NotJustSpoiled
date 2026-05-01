package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.network.MergeFoodPacket;
import net.therealvoin.notjustspoiled.network.NJSPacketHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow @Final public Player player;

    @Inject(method = "hasRemainingSpaceForItem", at = @At("HEAD"))
    private void makeFoodPossibleForMerging(ItemStack itemStack1, ItemStack itemStack2, CallbackInfoReturnable<Boolean> cir) {
        if (player.level() instanceof ServerLevel serverLevel) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack2, FoodEnvironment.INVENTORY, serverLevel);
            FoodSpoilageManager.tryAverageSpoilageOnMerge(itemStack1, itemStack2, serverLevel);
        } else {
            NJSPacketHandler.CHANNEL.sendToServer(new MergeFoodPacket(itemStack1, itemStack2));
        }
    }
}