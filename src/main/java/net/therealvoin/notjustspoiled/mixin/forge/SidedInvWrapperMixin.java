package net.therealvoin.notjustspoiled.mixin.forge;

import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
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
    private void mergeFood(Args args) {
        if (this.inv instanceof BlockEntity blockEntity) {
            FoodSpoilageManager.tryAverageSpoilageOnMerge(args.get(0), args.get(1), blockEntity.getLevel());
        }
    }
}