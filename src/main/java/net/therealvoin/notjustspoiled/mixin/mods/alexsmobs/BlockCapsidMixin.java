package net.therealvoin.notjustspoiled.mixin.mods.alexsmobs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "com.github.alexthe666.alexsmobs.block.BlockCapsid")
public abstract class BlockCapsidMixin {
    @WrapOperation(method = {"use", "m_6227_"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"))
    private void mergeFood(ItemStack stackInCapsid, int increment, Operation<Void> original, @Local(argsOnly = true) Level level, @Local(name = "copy") ItemStack stackInHand) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackInHand, FoodEnvironment.STORAGE, level);
        FoodSpoilageManager.tryAverageSpoilageOnMerge(stackInCapsid, stackInHand, level);

        original.call(stackInCapsid, increment);
    }
}