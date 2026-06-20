package net.therealvoin.notjustspoiled.mixin.mods.ends_delight;

import cn.foggyhillside.ends_delight.block.EndStoveBlock;
import cn.foggyhillside.ends_delight.blockentitiy.EndStoveBlockEntity;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndStoveBlockEntity.class)
public abstract class EndStoveBlockEntityMixin {
    @ModifyArg(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V"), index = 1, remap = false)
    private ItemStack changeFoodEnvironmentWhenPlacedOnStove(ItemStack stackToPlaceOnStove) {
        FoodEnvironment foodEnvironment = ((EndStoveBlockEntity)(Object)this).getBlockState().getValue(EndStoveBlock.LIT) ? FoodEnvironment.COOKING : FoodEnvironment.GROUND;
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPlaceOnStove, foodEnvironment, ((BlockEntity)(Object)this).getLevel());
        return stackToPlaceOnStove;
    }

    @Inject(method = "cookAndOutputItems", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/crafting/CampfireCookingRecipe;getResultItem(Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    private void copySpoilageCapToCookedFood(CallbackInfo ci, @Local(name = "stoveStack") ItemStack cookStack, @Local(name = "resultStack") ItemStack result) {
        NJSUtils.copyCapability(cookStack, result, ((BlockEntity)(Object)this).getLevel());
    }
}