package net.therealvoin.notjustspoiled.compat.mixin.alexsmobs;

import com.github.alexthe666.alexsmobs.tileentity.TileEntityCapsid;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityCapsid.class)
public abstract class TileEntityCapsidMixin {
    @Inject(method = "setItem", at = @At("TAIL"))
    private void updateFoodWhenPlacedInCapsid(int slot, ItemStack stack, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stack, FoodEnvironment.STORAGE, ((BlockEntity)(Object)this).getLevel());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;", ordinal = 1, shift = At.Shift.AFTER))
    private void createStackInCapsidSnapshot(CallbackInfo ci, @Local(name = "current") ItemStack stackInCapsid, @Share("stackInCapsidSnapshot") LocalRef<ItemStack> snapshot) {
        snapshot.set(stackInCapsid.copy());
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/alexsmobs/tileentity/TileEntityCapsid;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 2), index = 1)
    private ItemStack copySpoilageToResultStack(ItemStack stackToSetInCapsid, @Share("stackInCapsidSnapshot") LocalRef<ItemStack> snapshot) {
        FoodSpoilageManager.copySpoilage(snapshot.get(), FoodEnvironment.STORAGE, stackToSetInCapsid, ((BlockEntity)(Object)this).getLevel());
        return stackToSetInCapsid;
    }
}