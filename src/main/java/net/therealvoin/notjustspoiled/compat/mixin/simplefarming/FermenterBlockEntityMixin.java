package net.therealvoin.notjustspoiled.compat.mixin.simplefarming;

import dev.enemeez.simplefarming.common.block.entity.FermenterBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FermenterBlockEntity.class, remap = false)
public abstract class FermenterBlockEntityMixin {
    @Inject(method = {"setItem", "m_6836_"}, at = @At("TAIL"))
    private void changeFoodEnvironmentWhenPlacedInFermenter(int slot, ItemStack stack, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stack, FoodEnvironment.STORAGE, ((BlockEntity)(Object)this).getLevel());
    }
}