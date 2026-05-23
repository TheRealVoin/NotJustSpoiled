package net.therealvoin.notjustspoiled.mixin.mods.alexscaves;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.github.alexmodguy.alexscaves.server.block.blockentity.AbyssalAltarBlockEntity", remap = false)
public class AbyssalAltarBlockEntityMixin {
    @Inject(method = {"setItem", "m_6836_"}, at = @At("TAIL"))
    private void changeFoodEnvironmentWhenPlacedOnAbyssalAltar(int index, ItemStack stackToPutOnAbyssalAltar, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPutOnAbyssalAltar, FoodEnvironment.GROUND, ((BlockEntity)(Object)this).getLevel());
    }
}