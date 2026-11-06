package net.therealvoin.notjustspoiled.compat.mixin.farmersdelight;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

@Mixin(value = CuttingBoardRecipe.class, remap = false)
public abstract class CuttingBoardRecipeMixin {
    @ModifyArg(method = "rollResults", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private Object copySpoilageToResultStack(Object value, @Local(argsOnly = true) RecipeWrapper inventory) {
        ItemStack resultStack = (ItemStack) value;
        FoodSpoilageManager.copySpoilage(inventory.getItem(0), resultStack, NJSUtils.getLevelWithoutContext());
        return resultStack;
    }

    @ModifyReturnValue(method = "matches(Lnet/minecraftforge/items/wrapper/RecipeWrapper;Lnet/minecraft/world/level/Level;)Z", at = @At("RETURN"))
    private boolean isCraftingAllowed(boolean original, RecipeWrapper inventory, Level level) {
        return original && FoodSpoilageManager.isCraftingAllowed(inventory, level);
    }

    @ModifyReturnValue(method = "assemble(Lnet/minecraftforge/items/wrapper/RecipeWrapper;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"))
    private ItemStack copySpoilageToResultStack(ItemStack resultStack, @Local(argsOnly = true) RecipeWrapper container) {
        FoodSpoilageManager.manageCrafting(resultStack, container);
        return resultStack;
    }
}