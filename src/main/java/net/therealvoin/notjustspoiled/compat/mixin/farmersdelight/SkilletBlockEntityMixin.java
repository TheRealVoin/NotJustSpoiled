package net.therealvoin.notjustspoiled.compat.mixin.farmersdelight;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

@Mixin(SkilletBlockEntity.class)
public abstract class SkilletBlockEntityMixin {
    @Shadow public abstract boolean isHeated();

    @WrapOperation(method = "addItemToCook", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"), remap = false)
    private ItemStack changeFoodEnvironmentWhenPlacedInSkillet(ItemStackHandler instance, int slot, ItemStack stack, boolean simulate, Operation<ItemStack> original, @Local(argsOnly = true) Player player) {
        ItemStack remainderStack = original.call(instance, slot, stack, simulate);

        if (!ItemStack.matches(remainderStack, stack)) {
            FoodEnvironment foodEnvironment = this.isHeated() ? FoodEnvironment.COOKING : FoodEnvironment.OPEN_AIR;
            FoodSpoilageManager.changeEnvironmentAndUpdate(stack, foodEnvironment, player.level());
        }

        return remainderStack;
    }

    @Inject(method = "cookAndOutputItems", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/crafting/CampfireCookingRecipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    private void copySpoilageToResultStack(ItemStack cookingStack, Level level, CallbackInfo ci, @Local(name = "resultStack") ItemStack resultStack) {
        NJSUtils.copySpoilage(cookingStack, resultStack, level);
    }

    @Inject(method = "cookingTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/Mth;clamp(III)I", shift = At.Shift.AFTER))
    private static void changeFoodEnvironmentWhenSkilletBecomesCooled(Level level, BlockPos pos, BlockState state, SkilletBlockEntity skillet, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(skillet.getStoredStack(), FoodEnvironment.OPEN_AIR, level);
    }

    @Inject(method = "cookAndOutputItems", at = @At("HEAD"), remap = false)
    private void changeFoodEnvironmentWhenSkilletBecomesHeated(ItemStack cookingStack, Level level, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(cookingStack, FoodEnvironment.COOKING, level);
    }
}