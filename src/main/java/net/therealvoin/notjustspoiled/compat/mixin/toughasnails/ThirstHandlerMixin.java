package net.therealvoin.notjustspoiled.compat.mixin.toughasnails;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.therealvoin.notjustspoiled.common.data.foodstatus.FoodStatusEffectData;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import toughasnails.api.potion.TANEffects;
import toughasnails.thirst.ThirstHandler;

@Mixin(value = ThirstHandler.class, remap = false)
public abstract class ThirstHandlerMixin {
    @ModifyArg(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Ltoughasnails/api/thirst/IThirst;drink(IF)V"), index = 0)
    private static int modifyThirstValuesAndApplyEffects(int defaultThirst, @Local(name = "player") Player player, @Local(name = "drink") ItemStack itemStack) {
        if (player.level() instanceof ServerLevel serverLevel) {
            FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(itemStack, serverLevel);
            if (foodStatus == null) {
                return defaultThirst;
            }

            FoodSpoilageManager.updateFoodLifetime(itemStack, FoodEnvironment.INVENTORY, serverLevel);

            for (FoodStatusEffectData effect : foodStatus.getData().effects()) {
                if (player.getRandom().nextFloat() < effect.applyChance()) {
                    player.addEffect(
                            new MobEffectInstance(
                                    effect.effect() == MobEffects.HUNGER ? TANEffects.THIRST : effect.effect(),
                                    effect.duration(),
                                    effect.amplifier(),
                                    false,
                                    false
                            ),
                            player
                    );
                }
            }

            return foodStatus.getModifiedNutrition(defaultThirst);
        }

        return defaultThirst;
    }
}