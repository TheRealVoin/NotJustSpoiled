package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "tryToMerge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;areMergable(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void makeFoodPossibleForMerging(ItemEntity itemEntity2, CallbackInfo ci) {
        ItemEntity itemEntity1 = (ItemEntity) (Object) this;

        if (itemEntity1.level() instanceof ServerLevel serverLevel) {
            FoodSpoilageManager.tryAverageSpoilageOnMerge(itemEntity1.getItem(), itemEntity2.getItem(), serverLevel);
        }
    }
}