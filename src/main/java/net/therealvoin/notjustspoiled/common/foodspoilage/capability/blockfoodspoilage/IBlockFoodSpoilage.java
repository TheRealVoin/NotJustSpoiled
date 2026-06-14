package net.therealvoin.notjustspoiled.common.foodspoilage.capability.blockfoodspoilage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IBlockFoodSpoilage {
    ItemStack getLastItemStackByPos(BlockPos blockPos);
    void putItemStackPos(BlockPos blockPos, ItemStack itemStack);
    void removeItemStackPos(BlockPos blockPos, ItemStack itemStack);
}