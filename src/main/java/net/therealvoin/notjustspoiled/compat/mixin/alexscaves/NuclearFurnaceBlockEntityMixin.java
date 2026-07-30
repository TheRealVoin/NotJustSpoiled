package net.therealvoin.notjustspoiled.compat.mixin.alexscaves;

import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(NuclearFurnaceBlockEntity.class)
public abstract class NuclearFurnaceBlockEntityMixin {
    @Inject(method = "setItem", at = @At("TAIL"))
    private void updateFoodWhenPlacedInNuclearFurnace(int slot, ItemStack stack, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stack, FoodEnvironment.STORAGE, ((BlockEntity)(Object)this).getLevel());
    }

    @ModifyArgs(method = "setItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void removeSpoilageTagFromEqualityCheck(Args args) {
        NJSUtils.removeSpoilageTagFromEqualityCheck(args, ((BlockEntity)(Object)this).getLevel());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/crafting/AbstractCookingRecipe;getResultItem(Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    private static void copySpoilageToCookedFood(CallbackInfo ci, @Local(argsOnly = true) Level level, @Local(name = "cookResult") ItemStack resultSTack, @Local(name = "cookStack") ItemStack cookStack) {
        FoodSpoilageManager.copySpoilage(cookStack, FoodEnvironment.STORAGE, resultSTack, level);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V", ordinal = 0))
    private static void mergeFood(ItemStack stackInResultSlot, int increment, Operation<Void> originalMethod, @Local(argsOnly = true) Level level, @Local(name = "cookResult") ItemStack resultStack) {
        FoodSpoilageManager.averageFoodLifetimeBeforeMerge(stackInResultSlot, FoodEnvironment.STORAGE, resultStack, FoodEnvironment.STORAGE, increment, level);
        originalMethod.call(stackInResultSlot, increment);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", ordinal = 0))
    private static boolean addSpoilageTagToEqualityCheck(ItemStack stackInResultSlot, ItemStack resultStack, Operation<Boolean> originalMethod, @Local(argsOnly = true) Level level) {
        return NJSUtils.addSpoilageTagToEqualityCheck(stackInResultSlot, resultStack, originalMethod, level);
    }

    @WrapOperation(method = "canFitInResultSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean addSpoilageTagToEqualityCheck(ItemStack stackInResultSlot, ItemStack stackToSetInResultSlot, Operation<Boolean> originalMethod) {
        return NJSUtils.addSpoilageTagToEqualityCheck(stackInResultSlot, stackToSetInResultSlot, originalMethod, ((BlockEntity)(Object)this).getLevel());
    }
}