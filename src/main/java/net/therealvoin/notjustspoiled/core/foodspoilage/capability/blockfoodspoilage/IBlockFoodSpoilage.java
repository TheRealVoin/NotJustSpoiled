package net.therealvoin.notjustspoiled.core.foodspoilage.capability.blockfoodspoilage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IBlockFoodSpoilage {
    ItemStack getItemStackByPos(BlockPos blockPos);
    void putItemStackPos(BlockPos blockPos, ItemStack itemStack);
    void removeItemStackPos(BlockPos blockPos);
}