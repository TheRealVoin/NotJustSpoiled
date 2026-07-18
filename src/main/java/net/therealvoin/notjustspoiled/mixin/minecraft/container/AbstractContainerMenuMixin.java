package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.SimpleContainerAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Shadow @Final public NonNullList<Slot> slots;
    @Shadow private ItemStack carried;

    @Inject(method = "moveItemStackTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", shift = At.Shift.BEFORE))
    private void makeFoodPossibleForMerging(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) ItemStack stack, @Local Slot slot, @Local(ordinal = 1) ItemStack itemstack) {
        Level level = notJustSpoiled$getLevelBySlot(slot);

        if (level instanceof ServerLevel serverLevel) {
            FoodSpoilage foodSpoilage = FoodSpoilage.of(itemstack);

            if (foodSpoilage != null) {
                FoodSpoilageManager.changeEnvironmentAndUpdate(stack, foodSpoilage.getEnvironment(), serverLevel);
                FoodSpoilageManager.tryAverageSpoilageOnMerge(stack, itemstack, serverLevel);
            }
        }
    }

    @Inject(method = "setCarried", at = @At("HEAD"))
    private void changeFoodEnvironmentWhenPlacedInCarriedSlot(ItemStack itemStack, CallbackInfo ci) {
        Level level = null;
        for (Slot slot : this.slots) {
            level = notJustSpoiled$getLevelBySlot(slot);
            if (level != null) {
                break;
            }
        }

        if (level instanceof ServerLevel serverLevel) {
            if (this.carried.isEmpty() && !itemStack.isEmpty()) {
                FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, FoodEnvironment.INVENTORY, serverLevel);
            }
        }
    }

    @Inject(method = "canItemQuickReplace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private static void makeFoodPossibleForMerging(Slot slot, ItemStack stack, boolean stackSizeMatters, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemInSlot = slot.getItem();
        Level level = notJustSpoiled$getLevelBySlot(slot);

        if (level instanceof ServerLevel serverLevel) {
            FoodSpoilage foodSpoilage = FoodSpoilage.of(itemInSlot);
            if (foodSpoilage != null) {
                FoodSpoilageManager.changeEnvironmentAndUpdate(stack, foodSpoilage.getEnvironment(), serverLevel);
                FoodSpoilageManager.tryAverageSpoilageOnMerge(itemInSlot, stack, serverLevel);
            }
        }
    }

    @Unique
    private static Level notJustSpoiled$getLevelBySlot(Slot slot) {
        Container container = slot.container;
        if (container instanceof Inventory inventory) {
            return inventory.player.level();
        } else if (container instanceof BaseContainerBlockEntity blockEntity) {
            return blockEntity.getLevel();
        } else if (container instanceof AbstractMinecartContainer minecart) {
            return minecart.level();
        } else if (container instanceof ChestBoat chestBoat) {
            return chestBoat.level();
        } else if (container instanceof SimpleContainer simpleContainer) {
            AbstractHorse horse = ((SimpleContainerAccessor) simpleContainer).getHorse();
            if (horse != null) {
                return horse.level();
            }
        }

        return null;
    }
}