package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow @Final public Player player;

    @Inject(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void updateFood(int pSlot, ItemStack pStack, CallbackInfoReturnable<Integer> cir, Item item, int i, ItemStack itemStack) {
        if (player.level() instanceof ServerLevel serverLevel) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(pStack, FoodEnvironment.INVENTORY, serverLevel);
            FoodSpoilageManager.tryAverageSpoilageOnMerge(itemStack, pStack, serverLevel);
        }
    }
}