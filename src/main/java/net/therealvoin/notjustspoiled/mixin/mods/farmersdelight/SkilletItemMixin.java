package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.item.SkilletItem;

@Mixin(SkilletItem.class)
public class SkilletItemMixin {
    @Inject(method = "use", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    private void updateFoodWhenPlacedInSkillet(CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local(argsOnly = true) Level level, @Local(name = "cookingStackUnit") ItemStack cookingStackUnit) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(cookingStackUnit, FoodEnvironment.COOKING, level);
    }

    @Inject(method = "lambda$finishUsingItem$1", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/crafting/CampfireCookingRecipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;"))
    private static void copyCapToCookedFood(CallbackInfo ci, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) Level level, @Local(name = "resultStack") ItemStack resultStack) {
        ItemStack cookingStack = ItemStack.of(stack.getOrCreateTag().getCompound("Cooking"));
        NJSUtils.copySpoilage(cookingStack, resultStack, level);
    }
}