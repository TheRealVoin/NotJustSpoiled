package net.therealvoin.notjustspoiled.mixin.forge;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = SidedInvWrapper.class, remap = false)
public abstract class SidedInvWrapperMixin {
    @Shadow @Final protected WorldlyContainer inv;

    @ModifyArgs(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemHandlerHelper;canItemStacksStack(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void removeSpoilageTagFromEqualityCheck(Args args) {
        Level level = this.inv instanceof BlockEntity blockEntity ? blockEntity.getLevel() : NJSUtils.getLevelWithoutContext();
        NJSUtils.removeSpoilageTagFromEqualityCheck(args, level);
    }

    @WrapOperation(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"))
    private void averageFoodLifetimeBeforeMerge(ItemStack stackToSetInSlot, int increment, Operation<Void> originalMethod, @Local(name = "stackInSlot") ItemStack stackInSlot) {
        Level level = this.inv instanceof BlockEntity blockEntity ? blockEntity.getLevel() : NJSUtils.getLevelWithoutContext();
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInSlot, FoodEnvironment.STORAGE, stackToSetInSlot, FoodEnvironment.STORAGE, stackToSetInSlot.getCount(), level);
        originalMethod.call(stackToSetInSlot, increment);
    }
}