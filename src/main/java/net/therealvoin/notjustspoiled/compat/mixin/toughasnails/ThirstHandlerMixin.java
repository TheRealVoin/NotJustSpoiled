package net.therealvoin.notjustspoiled.compat.mixin.toughasnails;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import toughasnails.api.potion.TANEffects;
import toughasnails.thirst.ThirstHandler;

@Mixin(value = ThirstHandler.class, remap = false)
public abstract class ThirstHandlerMixin {
    @ModifyArgs(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Ltoughasnails/api/thirst/IThirst;drink(IF)V"))
    private static void modifyThirstValuesAndApplyEffects(Args args, @Local(name = "player") Player player, @Local(name = "drink") ItemStack itemStack) {
        if (player.level() instanceof ServerLevel serverLevel) {
            FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(itemStack, serverLevel);
            if (foodStatus == null) {
                return;
            }

            FoodSpoilageManager.updateFoodLifetime(itemStack, serverLevel);
            args.set(0, foodStatus.getModifiedNutrition(args.get(0)));
            args.set(1, foodStatus.getModifiedSaturation(args.get(1)));

            RandomSource random = serverLevel.getRandom();
            for (FoodStatus.EffectData effectData : foodStatus.getEffectsData()) {
                MobEffect effect = effectData.effect() == MobEffects.HUNGER ? TANEffects.THIRST : effectData.effect();

                if (random.nextDouble() < effectData.applyChance()) {
                    player.addEffect(effectData.createEffectInstance(effect), player);
                }
            }
        }
    }
}