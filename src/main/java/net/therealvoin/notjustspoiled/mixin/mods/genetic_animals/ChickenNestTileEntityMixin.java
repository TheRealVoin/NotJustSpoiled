package net.therealvoin.notjustspoiled.mixin.mods.genetic_animals;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "mokiyoki.enhancedanimals.tileentity.ChickenNestTileEntity")
public class ChickenNestTileEntityMixin {
    @Inject(method = "addEggToNest", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER))
    private void changeFoodEnvironmentWhenPlacedInChickenNest(Level level, ItemStack stackToPutInNest, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPutInNest, FoodEnvironment.GROUND, level);
    }

    @Inject(method = {"removeItem", "m_7407_"}, at = @At("RETURN"))
    private void changeFoodEnvironmentWhenPlacedInInventory(CallbackInfoReturnable<ItemStack> cir) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(cir.getReturnValue(), FoodEnvironment.INVENTORY, ((BlockEntity)(Object)this).getLevel());
    }
}