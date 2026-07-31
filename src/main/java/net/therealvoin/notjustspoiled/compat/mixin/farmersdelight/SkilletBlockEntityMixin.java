package net.therealvoin.notjustspoiled.compat.mixin.farmersdelight;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

@Mixin(SkilletBlockEntity.class)
public abstract class SkilletBlockEntityMixin {
    @Inject(method = "cookAndOutputItems", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/crafting/CampfireCookingRecipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    private void copySpoilageToResultStack(ItemStack cookingStack, Level level, CallbackInfo ci, @Local(name = "resultStack") ItemStack resultStack) {
        FoodSpoilageManager.copySpoilage(cookingStack, FoodEnvironment.OPEN_AIR, resultStack, level);
    }

    @Inject(method = "addItemToCook", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraftforge/items/ItemStackHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER), remap = false)
    private void changeFoodEnvironmentWhenPlaceInSkillet(CallbackInfoReturnable<ItemStack> cir, @Local(argsOnly = true) Player player, @Local(name = "wasEmpty") boolean wasEmpty) {
        if (wasEmpty) {
            FoodSpoilageManager.changeEnvironmentAndUpdate(((SkilletBlockEntity)(Object)this).getStoredStack(), FoodEnvironment.OPEN_AIR, player.level());
        }
    }
}