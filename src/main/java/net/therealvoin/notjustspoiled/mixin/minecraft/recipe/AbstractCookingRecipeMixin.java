package net.therealvoin.notjustspoiled.mixin.minecraft.recipe;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractCookingRecipe.class)
public abstract class AbstractCookingRecipeMixin {
    @ModifyReturnValue(method = "assemble", at = @At("RETURN"))
    private ItemStack copySpoilageToResultStack(ItemStack resultStack, @Local(argsOnly = true) Container container) {
        ItemStack inputStack = container.getItem(0);
        FoodSpoilageManager.copySpoilage(inputStack, resultStack, NJSUtils.getLevelWithoutContext());
        return resultStack;
    }
}