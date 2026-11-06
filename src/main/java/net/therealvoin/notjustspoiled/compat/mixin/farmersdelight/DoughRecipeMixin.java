package net.therealvoin.notjustspoiled.compat.mixin.farmersdelight;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vectorwing.farmersdelight.common.crafting.DoughRecipe;

@Mixin(value = DoughRecipe.class, remap = false)
public abstract class DoughRecipeMixin {
    @ModifyReturnValue(method = "matches(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/level/Level;)Z", at = @At("RETURN"))
    private boolean isCraftingAllowed(boolean original, CraftingContainer inventory, Level level) {
        return original && FoodSpoilageManager.isCraftingAllowed(inventory, level);
    }

    @ModifyReturnValue(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"))
    private ItemStack copySpoilageToResultStack(ItemStack resultStack, @Local(argsOnly = true) CraftingContainer container) {
        FoodSpoilageManager.manageCrafting(resultStack, container);
        return resultStack;
    }
}