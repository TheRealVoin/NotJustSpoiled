package net.therealvoin.notjustspoiled.mixin.mods.alexsmobs;

import com.github.alexthe666.alexsmobs.entity.EntityMantisShrimp;
import com.github.alexthe666.alexsmobs.entity.ai.MantisShrimpAIFryRice;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MantisShrimpAIFryRice.class)
public class MantisShrimpAIFryRiceMixin {
    @Shadow @Final private EntityMantisShrimp mantisShrimp;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/alexsmobs/entity/EntityMantisShrimp;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.BEFORE))
    private void copySpoilageCapToResultStack(CallbackInfo ci, @Local(name = "rice") ItemStack riceStack) {
        ItemStack stackInHand = mantisShrimp.getItemInHand(InteractionHand.MAIN_HAND);
        Level level = mantisShrimp.level();

        FoodSpoilageManager.changeEnvironmentAndUpdate(stackInHand, FoodEnvironment.INVENTORY, level);
        NJSUtils.copySpoilage(stackInHand, riceStack, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void changeFoodEnvironmentWhenMantisShrimpStartsCooking(CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(this.mantisShrimp.getItemInHand(InteractionHand.MAIN_HAND), FoodEnvironment.COOKING, this.mantisShrimp.level());
    }

    @Inject(method = "stop", at = @At("TAIL"))
    private void changeFoodEnvironmentWhenMantisShrimpStopsCooking(CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(this.mantisShrimp.getItemInHand(InteractionHand.MAIN_HAND), FoodEnvironment.INVENTORY, this.mantisShrimp.level());
    }
}