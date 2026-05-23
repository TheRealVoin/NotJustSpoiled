//package net.therealvoin.notjustspoiled.mixin.mods.farmersdelight;
//
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.Container;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
//import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
//import net.therealvoin.notjustspoiled.util.NJSUtils;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//import vectorwing.farmersdelight.common.block.StoveBlock;
//import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;
//import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;
//
//@Mixin(value = AbstractStoveBlockEntity.class)
//public class AbstractStoveBlockEntityMixin {
//    @Inject(method = "cookAndOutputItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isItemEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void copyCapabilityToCookedFood(CallbackInfo ci, boolean didChange, int i, ItemStack ingredient, Container container, ItemStack result) {
//        Level level = ((AbstractStoveBlockEntity) (Object) this).getLevel();
//
//        if (level instanceof ServerLevel serverLevel) {
//            NJSUtils.copyCapability(ingredient, result, serverLevel);
//        }
//    }
//
//    @Inject(method = "placeFood", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.BEFORE), remap = false)
//    private void updateFoodWhenPlacedOnStove(Entity entity, ItemStack foodStackToPlace, int foodCookingTime, CallbackInfoReturnable<Boolean> cir) {
//        Level level = ((AbstractStoveBlockEntity) (Object) this).getLevel();
//
//        if (level instanceof ServerLevel serverLevel) {
//            FoodEnvironment foodEnvironment = ((StoveBlockEntity) (Object) this).getBlockState().getValue(StoveBlock.LIT) ? FoodEnvironment.COOKING : FoodEnvironment.GROUND;
//            FoodSpoilageManager.changeEnvironmentAndUpdate(foodStackToPlace, foodEnvironment, serverLevel);
//        }
//    }
//}