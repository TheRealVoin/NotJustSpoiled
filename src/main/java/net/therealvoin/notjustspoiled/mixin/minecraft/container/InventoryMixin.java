package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow @Final public Player player;

    @Inject(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    private void changeFoodEnvironmentWhenPlacedInInventory(int pSlot, ItemStack pStack, CallbackInfoReturnable<Integer> cir) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(pStack, FoodEnvironment.INVENTORY, player.level());
    }

    @ModifyArg(method = "setItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private Object changeFoodEnvironmentWhenPlacedInInventory(Object itemStack) {
        FoodSpoilageManager.changeEnvironmentAndUpdate((ItemStack) itemStack, FoodEnvironment.INVENTORY, player.level());
        return itemStack;
    }

//    @WrapOperation(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"))
//    private void updateFood(ItemStack instance, int increment, Operation<Void> original, @Local(argsOnly = true) ItemStack pStack) {
//        Level level = player.level();
//
//        if (instance.isEmpty()) {
//            original.call(instance, increment);
//            FoodSpoilageManager.changeEnvironmentAndUpdate(instance, FoodEnvironment.INVENTORY, level);
//        } else {
//            FoodSpoilageManager.changeEnvironmentAndUpdate(pStack, FoodEnvironment.INVENTORY, level);
//            FoodSpoilageManager.tryAverageSpoilageOnMerge(instance, pStack, level);
//            original.call(instance, increment);
//        }
//    }

//    @ModifyArgs(method = "hasRemainingSpaceForItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
//    private void s(Args args) {
//        ItemStack itemStack1 = args.get(0);
//        ItemStack itemStack2 = args.get(1);
//
//        if (!FoodSpoilageManager.isInitialized(itemStack1) || !FoodSpoilageManager.isInitialized(itemStack2)) {
//            return;
//        }
//
//        if (!itemStack1.hasTag() || !itemStack1.getTag().contains("food_spoilage") || !itemStack2.hasTag() || !itemStack2.getTag().contains("food_spoilage")) {
//            return;
//        }
//
//        Level level = player.level();
//        FoodStatus foodStatus1 = FoodSpoilageManager.getFoodStatus(itemStack1, level);
//        FoodStatus foodStatus2 = FoodSpoilageManager.getFoodStatus(itemStack2, level);
//
//        if (foodStatus1 != foodStatus2) {
//            return;
//        }
//
//        ItemStack stackWithoutSpoilageTag1 = itemStack1.copy();
//        ItemStack stackWithoutSpoilageTag2 = itemStack2.copy();
//        stackWithoutSpoilageTag1.getTag().remove("food_spoilage");
//        stackWithoutSpoilageTag2.getTag().remove("food_spoilage");
//
//        args.set(0, stackWithoutSpoilageTag1);
//        args.set(1, stackWithoutSpoilageTag2);
//    }
}