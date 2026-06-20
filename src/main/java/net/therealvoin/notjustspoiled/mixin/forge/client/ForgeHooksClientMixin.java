package net.therealvoin.notjustspoiled.mixin.forge.client;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.therealvoin.notjustspoiled.common.foodspoilage.capability.foodspoilage.FoodSpoilageProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ForgeHooksClient.class, remap = false)
public abstract class ForgeHooksClientMixin {
    @Inject(method = "shouldCauseReequipAnimation", at = @At("RETURN"), cancellable = true)
    private static void cancelRedundantReequipAnimationWhenCapabilityChanged(ItemStack from, ItemStack to, int slot, CallbackInfoReturnable<Boolean> cir) {
        from.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage1 -> {
            to.getCapability(FoodSpoilageProvider.FOOD_SPOILAGE).ifPresent(foodSpoilage2 -> {
                cir.setReturnValue(false);
            });
        });
    }
}