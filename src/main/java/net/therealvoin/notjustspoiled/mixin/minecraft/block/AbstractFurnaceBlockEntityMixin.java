package net.therealvoin.notjustspoiled.mixin.minecraft.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Shadow protected NonNullList<ItemStack> items;

    @ModifyArgs(method = "setItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void removeSpoilageTagFromEqualityCheck(Args args) {
        NJSUtils.removeSpoilageTagFromEqualityCheck(args, ((BlockEntity)(Object)this).getLevel());
    }

    @Inject(method = "burn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/crafting/Recipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    private void copySpoilageToCookedFood(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) NonNullList<ItemStack> inventory, @Local(ordinal = 0) ItemStack stackInCookSlot, @Local(ordinal = 1) ItemStack resultStack) {
        FoodSpoilageManager.copySpoilage(stackInCookSlot, FoodEnvironment.STORAGE, resultStack, ((BlockEntity)(Object)this).getLevel());
    }

    @ModifyArg(method = "burn", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;", ordinal = 0), index = 1)
    private Object changeFoodEnvironmentWhenPlacedInResultSlot(Object value) {
        ItemStack stackToPlaceInResultSlot = (ItemStack) value;
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPlaceInResultSlot, FoodEnvironment.STORAGE, ((BlockEntity)(Object)this).getLevel());
        return stackToPlaceInResultSlot;
    }

    @WrapOperation(method = "burn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"))
    private void averageFoodLifetimeBeforeMerge(ItemStack stackInResultSlot, int increment, Operation<Void> originalMethod, @Local(ordinal = 1) ItemStack resultStack) {
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInResultSlot, FoodEnvironment.STORAGE, resultStack, FoodEnvironment.STORAGE, increment, ((BlockEntity)(Object)this).getLevel());
        originalMethod.call(stackInResultSlot, increment);
    }

    @WrapOperation(method = "canBurn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean addSpoilageTagToEqualityCheck(ItemStack stackInResultSlot, ItemStack resultStack, Operation<Boolean> originalMethod) {
        Level level = ((BlockEntity)(Object)this).getLevel();
        FoodSpoilageManager.copySpoilage(this.items.get(0), FoodEnvironment.STORAGE, resultStack, level);

        return NJSUtils.addSpoilageTagToEqualityCheck(stackInResultSlot, resultStack, originalMethod, level);
    }
}