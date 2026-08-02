package net.therealvoin.notjustspoiled.mixin.forge;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMobEffectInstance;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import net.therealvoin.notjustspoiled.common.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = IForgeMobEffectInstance.class, remap = false)
public interface IForgeMobEffectInstanceMixin {
    @Shadow List<ItemStack> getCurativeItems();

    /**
     * @author TheRealVoin
     * @reason Injectors are unsupported in interface mixins so the only solution is to overwrite the original method
     */
    @Overwrite
    default boolean isCurativeItem(ItemStack stack) {
        if (this.getCurativeItems().stream().noneMatch(e -> ItemStack.isSameItem(e, stack))) {
            return false;
        }

        FoodStatus foodStatus = FoodSpoilageManager.getFoodStatus(stack, NJSUtils.getLevelWithoutContext());
        return foodStatus != FoodStatus.HALF_SPOILED && foodStatus != FoodStatus.SPOILED;
    }
}