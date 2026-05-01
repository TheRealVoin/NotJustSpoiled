package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

@Mixin(value = CuttingBoardBlockEntity.class, remap = false)
public class CuttingBoardBlockEntityMixin {
    @ModifyArg(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V"), index = 1)
    private ItemStack changeFoodEnvironmentWhenPlacedOnCuttingBoard (ItemStack stack) {
        Level level = ((CuttingBoardBlockEntity) (Object) this).getLevel();
        if (level instanceof ServerLevel serverLevel) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(stack, FoodEnvironment.GROUND, serverLevel);
        }

        return stack;
    }
}