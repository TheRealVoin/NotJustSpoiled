package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

@Mixin(value = CuttingBoardBlockEntity.class, remap = false)
public class CuttingBoardBlockEntityMixin {
    @Inject(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.AFTER))
    private void changeFoodEnvironmentWhenPlacedOnCuttingBoard(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        Level level = ((CuttingBoardBlockEntity) (Object) this).getLevel();
        if (level.isClientSide()) {
            return;
        }

        FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, FoodEnvironment.GROUND, level);
    }
}