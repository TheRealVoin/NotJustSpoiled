package net.therealvoin.notjustspoiled.mixin.mods.alexsmobs;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.github.alexthe666.alexsmobs.tileentity.TileEntityCapsid")
public abstract class TileEntityCapsidMixin {
    @Inject(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;", ordinal = 0, shift = At.Shift.AFTER), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V")))
    private void createStackInCapsidSnapshot(CallbackInfo ci, @Local(name = "current") ItemStack stackInCapsid, @Share("stackInCapsidSnapshot") LocalRef<ItemStack> snapshot) {
        snapshot.set(stackInCapsid.copy());
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/alexsmobs/tileentity/TileEntityCapsid;setItem(ILnet/minecraft/world/item/ItemStack;)V"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V")), index = 1)
    private ItemStack copySpoilageCapToResultStack(ItemStack stackToSetInCapsid, @Share("stackInCapsidSnapshot") LocalRef<ItemStack> snapshot) {
        NJSUtils.copyCapability(snapshot.get(), stackToSetInCapsid, ((BlockEntity)(Object)this).getLevel());
        return stackToSetInCapsid;
    }

    @Inject(method = {"setItem", "m_6836_"}, at = @At("TAIL"))
    private void changeFoodEnvironmentWhenPlacedInCapsid(int index, ItemStack stackToSetInCapsid, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToSetInCapsid, FoodEnvironment.STORAGE, ((BlockEntity)(Object)this).getLevel());
    }
}