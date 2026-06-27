package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.therealvoin.notjustspoiled.common.config.FoodCraftingMode;
import net.therealvoin.notjustspoiled.common.config.NJSServerConfig;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodCategory;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin {
    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.AFTER))
    private static void updateFoodWhenPlacedIntoCraftingSlotAndManageCrafting(AbstractContainerMenu menu, Level level, Player player, CraftingContainer container, ResultContainer result, CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel) {
            FoodCraftingMode craftingMode = NJSServerConfig.FOOD_CRAFTING_MODE.get();

            if (craftingMode == FoodCraftingMode.SAME_STATUS || craftingMode == FoodCraftingMode.FRESH_STATUS || craftingMode == FoodCraftingMode.FRESH_OR_STALE_STATUS) {
                for (int i = 0; i < container.getContainerSize(); i++) {
                    ItemStack itemStack1 = container.getItem(i);

                    FoodCategory category1 = FoodCategory.getFoodCategory(itemStack1);
                    if (itemStack1.isEmpty() || category1 == null) {
                        continue;
                    }

                    FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack1);
                    if (foodSpoilage == null) {
                        continue;
                    }

                    for (int j = 0; j < container.getContainerSize(); j++) {
                        ItemStack itemStack2 = container.getItem(j);

                        FoodCategory category2 = FoodCategory.getFoodCategory(itemStack2);
                        if (itemStack2.isEmpty() || category2 == null) {
                            continue;
                        }

                        FoodSpoilage foodSpoilage2 = FoodSpoilage.of(itemStack2);
                        if (foodSpoilage2 == null) {
                            continue;
                        }

                        FoodSpoilageManager.updateFoodLifetime(foodSpoilage, serverLevel);
                        FoodSpoilageManager.updateFoodLifetime(foodSpoilage2, serverLevel);

                        FoodStatus foodStatus1 = FoodSpoilageManager.getFoodStatus(itemStack1, serverLevel);
                        FoodStatus foodStatus2 = FoodSpoilageManager.getFoodStatus(itemStack2, serverLevel);
                        boolean condition = false;
                        switch (craftingMode) {
                            case SAME_STATUS -> condition = foodStatus1 != foodStatus2;
                            case FRESH_STATUS -> condition = foodStatus1 != FoodStatus.FRESH || foodStatus2 != FoodStatus.FRESH;
                            case FRESH_OR_STALE_STATUS -> condition = foodStatus1 == FoodStatus.HALF_SPOILED || foodStatus1 == FoodStatus.SPOILED || foodStatus2 == FoodStatus.HALF_SPOILED || foodStatus2 == FoodStatus.SPOILED;
                        }

                        if (condition) {
                            result.setItem(0, ItemStack.EMPTY);
                            return;
                        }
                    }
                }
            }
        }
    }
}