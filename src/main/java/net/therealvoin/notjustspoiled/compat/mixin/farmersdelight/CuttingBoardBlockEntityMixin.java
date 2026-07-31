package net.therealvoin.notjustspoiled.compat.mixin.farmersdelight;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
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

    @WrapOperation(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack changeFoodEnvironmentWhenPlacedOnCuttingBoard(ItemStackHandler inventory, int slot, ItemStack stack, boolean simulate, Operation<ItemStack> originalMethod) {
        CuttingBoardBlockEntity blockEntity = ((CuttingBoardBlockEntity)(Object)this);
        if (blockEntity.getStoredItem().isEmpty()) {
            ItemStack remainderStack = originalMethod.call(inventory, slot, stack, simulate);
            FoodSpoilageManager.changeEnvironmentAndUpdate(blockEntity.getStoredItem(), FoodEnvironment.OPEN_AIR, blockEntity.getLevel());
            return remainderStack;
        }

        return originalMethod.call(inventory, slot, stack, simulate);
    }

    @ModifyArg(method = "lambda$processStoredItemUsingTool$2", at = @At(value = "INVOKE", target = "Lvectorwing/farmersdelight/common/utility/ItemUtils;spawnItemEntity(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;DDDDDD)V"), index = 1)
    private ItemStack copySpoilageToResultStack(ItemStack stack) {
        FoodSpoilageManager.copySpoilage(this.getStoredItem(), FoodEnvironment.OPEN_AIR, stack, ((BlockEntity)(Object)this).getLevel());
        return stack;
    }
}