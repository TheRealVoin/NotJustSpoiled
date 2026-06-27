package net.therealvoin.notjustspoiled.mixin.forge.client;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ForgeHooksClient.class, remap = false)
public abstract class ForgeHooksClientMixin {
    @Inject(method = "shouldCauseReequipAnimation", at = @At("RETURN"), cancellable = true)
    private static void cancelRedundantReequipAnimationWhenCapabilityChanged(ItemStack from, ItemStack to, int slot, CallbackInfoReturnable<Boolean> cir) {
        FoodSpoilage foodSpoilage1 = FoodSpoilage.of(from);
        FoodSpoilage foodSpoilage2 = FoodSpoilage.of(to);

        if (foodSpoilage1 != null && foodSpoilage2 != null) {
            cir.setReturnValue(false);
        }
    }
}