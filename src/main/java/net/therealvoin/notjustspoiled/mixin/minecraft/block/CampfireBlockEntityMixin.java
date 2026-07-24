package net.therealvoin.notjustspoiled.mixin.minecraft.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {
    @ModifyArg(method = "cookTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Containers;dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V"), index = 4)
    private static ItemStack copySpoilageToCookedFood(ItemStack resultStack, @Local(argsOnly = true) Level level, @Local(ordinal = 0) ItemStack inputStack) {
        FoodSpoilageManager.copySpoilage(inputStack, FoodEnvironment.COOKING, resultStack, level);
        return resultStack;
    }

    @ModifyArg(method = "placeFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private Object changeFoodEnvironmentWhenPlacedOnCampfire(Object value) {
        ItemStack stackToPlaceOnCampfire = (ItemStack) value;
        CampfireBlockEntity campfire = (CampfireBlockEntity) (Object) this;
        FoodEnvironment environment = campfire.getBlockState().getValue(CampfireBlock.LIT) ? FoodEnvironment.COOKING : FoodEnvironment.OPEN_AIR;
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToPlaceOnCampfire, environment, campfire.getLevel());
        return stackToPlaceOnCampfire;
    }
}