package net.therealvoin.notjustspoiled.mixin.minecraft.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {
    @Inject(method = "cookTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Containers;dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.BEFORE))
    private static void copySpoilageCapToCookedFood(CallbackInfo ci, @Local(argsOnly = true) Level level, @Local(ordinal = 0) ItemStack inputItem, @Local(ordinal = 1) ItemStack resultItem) {
        NJSUtils.copySpoilage(inputItem, resultItem, level);
    }

    @WrapOperation(method = "placeFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack changeFoodEnvironmentWhenPlacedOnCampfire(ItemStack stackInHand, int amount, Operation<ItemStack> original) {
        ItemStack stackToPlaceOnCampfire = original.call(stackInHand, amount);
        CampfireBlockEntity campfire = (CampfireBlockEntity) (Object) this;
        FoodEnvironment foodEnvironment = campfire.getBlockState().getValue(CampfireBlock.LIT) ? FoodEnvironment.COOKING : FoodEnvironment.OPEN_AIR;
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPlaceOnCampfire, foodEnvironment, campfire.getLevel());

        return stackToPlaceOnCampfire;
    }
}