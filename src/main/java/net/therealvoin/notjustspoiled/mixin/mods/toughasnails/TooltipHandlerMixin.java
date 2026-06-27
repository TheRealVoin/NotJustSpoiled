package net.therealvoin.notjustspoiled.mixin.mods.toughasnails;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import toughasnails.client.handler.TooltipHandler;

@Mixin(TooltipHandler.class)
public abstract class TooltipHandlerMixin {
    @ModifyArg(method = "onRenderTooltip", at = @At(value = "INVOKE", target = "Ltoughasnails/client/handler/TooltipHandler$ThirstClientTooltipComponent;<init>(I)V"))
    private static int d(int amount, @Local(name = "stack") ItemStack itemStack) {
        FoodStatus currentFoodStatus = FoodSpoilageManager.getFoodStatus(itemStack, Minecraft.getInstance().level);
        if (currentFoodStatus == null) {
            return amount;
        }

        return currentFoodStatus.getModifiedNutrition(amount);
    }
}