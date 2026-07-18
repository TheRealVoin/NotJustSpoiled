package net.therealvoin.notjustspoiled.compat.mixin.farmersdelight;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

@Mixin(value = CuttingBoardBlockEntity.class, remap = false)
public abstract class CuttingBoardBlockEntityMixin {
    @Shadow public abstract ItemStack getStoredItem();

    @ModifyArg(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"), index = 1)
    private ItemStack changeEnvironmentWhenPlacedOnCuttingBoard(ItemStack stackToPlaceOnCuttingBoard) {
        Level level = ((BlockEntity)(Object)this).getLevel();

        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPlaceOnCuttingBoard, FoodEnvironment.OPEN_AIR, level);

        return stackToPlaceOnCuttingBoard;
    }

    @ModifyArg(method = "lambda$processStoredItemUsingTool$2", at = @At(value = "INVOKE", target = "Lvectorwing/farmersdelight/common/utility/ItemUtils;spawnItemEntity(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;DDDDDD)V"), index = 1)
    private ItemStack copyCapToResultStack(ItemStack stack) {
        FoodSpoilageManager.copySpoilage(this.getStoredItem(), stack, ((BlockEntity)(Object)this).getLevel());
        return stack;
    }
}