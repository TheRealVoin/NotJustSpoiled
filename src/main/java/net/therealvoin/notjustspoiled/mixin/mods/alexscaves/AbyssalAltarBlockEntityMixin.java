package net.therealvoin.notjustspoiled.mixin.mods.alexscaves;

import com.github.alexmodguy.alexscaves.server.block.blockentity.AbyssalAltarBlockEntity;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbyssalAltarBlockEntity.class)
public abstract class AbyssalAltarBlockEntityMixin {
    @Inject(method = "setItem", at = @At("TAIL"))
    private void changeFoodEnvironmentWhenPlacedOnAbyssalAltar(CallbackInfo ci, @Local(argsOnly = true) ItemStack stackToPutOnAbyssalAltar) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPutOnAbyssalAltar, FoodEnvironment.OPEN_AIR, ((BlockEntity)(Object)this).getLevel());
    }
}