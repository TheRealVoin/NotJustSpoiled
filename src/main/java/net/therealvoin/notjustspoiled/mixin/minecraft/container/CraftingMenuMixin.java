package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.core.config.FoodCraftingMode;
import net.therealvoin.notjustspoiled.core.config.NJSServerConfig;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.core.foodspoilage.capability.foodspoilage.IFoodSpoilage;
import net.therealvoin.notjustspoiled.util.NJSUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {
    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.AFTER))
    private static void updateFoodWhenPlacedIntoCraftingSlotAndManageCrafting(AbstractContainerMenu menu, Level level, Player player, CraftingContainer container, ResultContainer result, CallbackInfo ci) {
        if (NJSServerConfig.FOOD_CRAFTING_MODE.get() == FoodCraftingMode.SAME_STATUS) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack itemStack1 = container.getItem(i);

                FoodCategory category1 = FoodCategory.getFoodCategory(itemStack1);
                if (itemStack1.isEmpty() || category1 == null) {
                    continue;
                }

                IFoodSpoilage foodSpoilage = NJSUtils.getCapability(itemStack1);
                if (foodSpoilage == null) {
                    continue;
                }

                for (int j = 0; j < container.getContainerSize(); j++) {
                    ItemStack itemStack2 = container.getItem(j);

                    FoodCategory category2 = FoodCategory.getFoodCategory(itemStack2);
                    if (itemStack2.isEmpty() || category2 == null) {
                        continue;
                    }

                    IFoodSpoilage foodSpoilage2 = NJSUtils.getCapability(itemStack2);
                    if (foodSpoilage2 == null) {
                        continue;
                    }

                    FoodSpoilageManager.updateFoodLifetime(foodSpoilage, level);
                    FoodSpoilageManager.updateFoodLifetime(foodSpoilage2, level);

                    if (FoodSpoilageManager.getFoodStatus(itemStack1, level) != FoodSpoilageManager.getFoodStatus(itemStack2, level)) {
                        result.setItem(0, ItemStack.EMPTY);
                        return;
                    }
                }
            }
        }
    }
}