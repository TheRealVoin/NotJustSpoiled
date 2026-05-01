package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;

import net.minecraft.server.level.ServerLevel;
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

@Mixin(StoveBlockEntity.class)
public class StoveBlockEntityMixin {
    @Inject(method = "cookAndOutputItems", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/crafting/CampfireCookingRecipe;getResultItem(Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void copyCapabilityToCookedFood(CallbackInfo ci, boolean didInventoryChange, int i, ItemStack stoveStack, Container inventoryWrapper, Optional<CampfireCookingRecipe> recipe, ItemStack resultStack) {
        Level level = ((StoveBlockEntity) (Object) this).getLevel();

        if (level instanceof ServerLevel serverLevel) {
            NJSUtils.copyCapability(stoveStack, resultStack, serverLevel);
        }
    }

    @Inject(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.BEFORE), remap = false)
    private void updateFoodWhenPlacedOnStove(ItemStack itemStackIn, CampfireCookingRecipe recipe, int slot, CallbackInfoReturnable<Boolean> cir) {
        Level level = ((StoveBlockEntity) (Object) this).getLevel();

        if (level instanceof ServerLevel serverLevel) {
            FoodEnvironment foodEnvironment = ((StoveBlockEntity) (Object) this).getBlockState().getValue(StoveBlock.LIT) ? FoodEnvironment.COOKING : FoodEnvironment.GROUND;
            FoodSpoilageManager.changeEnvironmentAndUpdate(itemStackIn, foodEnvironment, serverLevel);
        }
    }
}