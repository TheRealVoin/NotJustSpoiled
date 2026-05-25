//package net.therealvoin.notjustspoiled.mixin.mods.alexscaves;
//
//import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import com.llamalad7.mixinextras.sugar.Local;
//import com.llamalad7.mixinextras.sugar.Share;
//import com.llamalad7.mixinextras.sugar.ref.LocalRef;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
//import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
//import net.therealvoin.notjustspoiled.mixin.mods.alexscaves.accessor.NuclearFurnaceBlockEntityAccessor;
//import net.therealvoin.notjustspoiled.util.NJSUtils;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Pseudo;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.ModifyArg;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Pseudo
//@Mixin(targets = "com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity")
//public class NuclearFurnaceBlockEntityMixin {
//    @Inject(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/NonNullList;get(I)Ljava/lang/Object;", ordinal = 1, shift = At.Shift.BEFORE))
//    private static void createInputStackSnapshot(CallbackInfo ci, @Local(name = "cookStack") ItemStack inputStack, @Share("inputStackSnapshot") LocalRef<ItemStack> snapshot) {
//        snapshot.set(inputStack.copy());
//    }
//
//    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V", ordinal = 0))
//    private static void mergeFood(ItemStack stackInResultSlot, int increment, Operation<Void> original, @Local(argsOnly = true) Level level, @Local(name = "cookResult") ItemStack resultStack, @Share("inputStackSnapshot") LocalRef<ItemStack> inputStack) {
//        NJSUtils.copyCapability(inputStack.get(), resultStack, level);
//
//        FoodSpoilageManager.changeEnvironmentAndUpdate(resultStack, FoodEnvironment.STORAGE, level);
//        FoodSpoilageManager.tryAverageSpoilageOnMerge(stackInResultSlot, resultStack, level);
//
//        original.call(stackInResultSlot, increment);
//    }
//
//    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/NuclearFurnaceBlockEntity;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 0), index = 1)
//    private static ItemStack changeFoodEnvironmentWhenPlacedInResultSlot(ItemStack stackToSetInResultSlot, @Local(argsOnly = true) Level level, @Share("inputStackSnapshot") LocalRef<ItemStack> inputStack) {
//        NJSUtils.copyCapability(inputStack.get(), stackToSetInResultSlot, level);
//        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToSetInResultSlot, FoodEnvironment.STORAGE, level);
//
//        return stackToSetInResultSlot;
//    }
//
//    @Inject(method = "tick", at = @At("TAIL"))
//    private static void changeFoodEnvironmentWhenFurnaceBecomesLitOrUnlit(CallbackInfo ci, @Local(argsOnly = true) NuclearFurnaceBlockEntity blockEntity) {
//        FoodEnvironment foodEnvironment = FoodEnvironment.STORAGE;
//
//        if (((NuclearFurnaceBlockEntityAccessor)(blockEntity)).getCookTime() > 0) {
//            foodEnvironment = FoodEnvironment.COOKING;
//        }
//
//        FoodSpoilageManager.changeEnvironmentAndUpdate(blockEntity.getItem(0), foodEnvironment, blockEntity.getLevel());
//    }
//}