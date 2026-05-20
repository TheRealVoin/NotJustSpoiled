package net.therealvoin.notjustspoiled.mixin.mods.toughasnails;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "toughasnails.block.entity.WaterPurifierBlockEntity")
public abstract class WaterPurifierBlockEntityMixin {
    @ModifyArg(method = {"setItem", "m_6836_"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private Object changeEnvironmentWhenPlacedInWaterPurifier(Object stackToSetInWaterPurifier) {
        FoodSpoilageManager.changeEnvironmentAndUpdate((ItemStack) stackToSetInWaterPurifier, FoodEnvironment.STORAGE, ((BlockEntity)(Object)this).getLevel());
        return stackToSetInWaterPurifier;
    }
}