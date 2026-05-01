package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vectorwing.farmersdelight.common.item.SkilletItem;

import java.util.Optional;

@Mixin(value = SkilletItem.class, remap = false)
public class SkilletItemMixin {
    @Inject(method = "use", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void updateFoodWhenPlacedInSkillet(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, ItemStack skilletStack, InteractionHand otherHand, ItemStack cookingStack, Optional<CampfireCookingRecipe> recipe, ItemStack cookingStackCopy, ItemStack cookingStackUnit) {
        if (level instanceof ServerLevel serverLevel) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(cookingStackUnit, FoodEnvironment.COOKING, serverLevel);
        }
    }
}