package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;

import java.util.Optional;

@Mixin(value = StoveBlockEntity.class, remap = false)
public class StoveBlockEntityMixin {
    @Inject(method = "cookAndOutputItems", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/crafting/CampfireCookingRecipe;getResultItem(Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void copyCapabilityToCookedFood(CallbackInfo ci, boolean didInventoryChange, int i, ItemStack stoveStack, Container inventoryWrapper, Optional<CampfireCookingRecipe> recipe, ItemStack resultStack) {
        Level level = ((StoveBlockEntity) (Object) this).getLevel();

        if (level.isClientSide()) {
            return;
        }

        NJSUtils.copyCapability(stoveStack, resultStack, level);
    }

    @Inject(method = "addItem", at = @At(value = "RETURN", ordinal = 0))
    private void updateFoodWhenPlacedOnStove(ItemStack itemStackIn, CampfireCookingRecipe recipe, int slot, CallbackInfoReturnable<Boolean> cir) {
        Level level = ((StoveBlockEntity) (Object) this).getLevel();

        if (level.isClientSide()) {
            return;
        }

        FoodEnvironment foodEnvironment = ((StoveBlockEntity) (Object) this).getBlockState().getValue(StoveBlock.LIT) ? FoodEnvironment.COOKING : FoodEnvironment.GROUND;
        FoodSpoilageManager.changeEnvironmentAndUpdate(itemStackIn, foodEnvironment, level);
    }
}