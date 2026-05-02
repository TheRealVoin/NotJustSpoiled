package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow @Final public Player player;

    @Redirect(method = "hasRemainingSpaceForItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean makeFoodPossibleForMerging(ItemStack itemStack1, ItemStack itemStack2) {
        if (player.level() instanceof ServerLevel serverLevel) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack2, FoodEnvironment.INVENTORY, serverLevel);
            FoodSpoilageManager.tryAverageSpoilageOnMerge(itemStack1, itemStack1, serverLevel);
        }

        return ItemStack.isSameItemSameTags(itemStack1, itemStack2);
    }
}