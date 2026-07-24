package net.therealvoin.notjustspoiled.mixin.minecraft.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.therealvoin.notjustspoiled.common.foodspoilage.BlockFoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CakeBlock.class)
public abstract class CakeBlockMixin {
    @ModifyArg(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
    private static int modifyFoodValuesAndApplyEffects(int defaultNutrition, @Local(argsOnly = true) BlockPos blockPos, @Local(argsOnly = true) BlockState blockState, @Local(argsOnly = true) Player player) {
        Level level = player.level();

        if (level.isClientSide()) {
            return defaultNutrition;
        }

        BlockFoodSpoilage data = BlockFoodSpoilage.get((ServerLevel) level);
        ItemStack cakeStack = data.getLastItemStackAt(blockPos);

        FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(cakeStack, level);
        if (foodStatus == null) {
            return defaultNutrition;
        }

        FoodSpoilageManager.updateFoodLifetime(cakeStack, FoodEnvironment.OPEN_AIR, level);
        foodStatus.applyEffects(player);

        if (blockState.getValue(CakeBlock.BITES) >= CakeBlock.MAX_BITES) {
            data.removeItemStackAt(blockPos, cakeStack);
        }

        return foodStatus.getModifiedNutrition(defaultNutrition);
    }
}