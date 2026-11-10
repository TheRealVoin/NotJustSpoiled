package net.therealvoin.notjustspoiled.mixin.minecraft;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.therealvoin.notjustspoiled.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.util.NJSUtils;
import net.therealvoin.notjustspoiled.util.SimpleContainerAccessor;
import net.therealvoin.notjustspoiled.mixin.minecraft.recipe.accessor.TransientCraftingContainerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RandomizableContainerBlockEntity.class, ChestBoat.class, AbstractMinecartContainer.class, SimpleContainer.class, AbstractFurnaceBlockEntity.class, HopperBlockEntity.class, BrewingStandBlockEntity.class, TransientCraftingContainer.class})
public abstract class SetItemMixin {
    @Inject(method = "setItem", at = @At("TAIL"))
    private void changeFoodEnvironmentWhenPlacedInContainer(int index, ItemStack itemStack, CallbackInfo ci) {
        Object object = this;
        Level level = null;
        FoodEnvironment foodEnvironment = FoodEnvironment.STORAGE;

        if (object instanceof AbstractMinecartContainer minecartContainer) {
            level = minecartContainer.level();
        } else if (object instanceof BlockEntity blockEntity) {
            level = blockEntity.getLevel();
        } else if (object instanceof SimpleContainer simpleContainer) {
            AbstractHorse horse = ((SimpleContainerAccessor) simpleContainer).getHorse();
            if (horse != null) {
                level = horse.level();
            }
        } else if (object instanceof ChestBoat chestBoat) {
            level = chestBoat.level();
        } else if (object instanceof TransientCraftingContainer container) {
            foodEnvironment = ((TransientCraftingContainerAccessor)(container)).getAbstractContainerMenu() instanceof InventoryMenu ? FoodEnvironment.INVENTORY : foodEnvironment;
            level = NJSUtils.getLevelWithoutContext();
        }

        if (level == null) {
            return;
        }

        FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, foodEnvironment, level);
    }
}