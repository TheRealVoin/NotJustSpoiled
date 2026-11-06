package net.therealvoin.notjustspoiled.mixin.minecraft.recipe.accessor;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TransientCraftingContainer.class)
public interface TransientCraftingContainerAccessor {
    @Accessor("menu") AbstractContainerMenu getAbstractContainerMenu();
}