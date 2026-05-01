package net.therealvoin.notjustspoiled.core.foodspoilage.capability.blockfoodspoilage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BlockFoodSpoilageProvider implements IBlockFoodSpoilage, ICapabilitySerializable<CompoundTag> {
    public static final Capability<IBlockFoodSpoilage> BLOCK_FOOD_SPOILAGE = CapabilityManager.get(new CapabilityToken<>() {});
    private final LazyOptional<IBlockFoodSpoilage> optional = LazyOptional.of(() -> this);

    private final Map<BlockPos, List<ItemStack>> blocks = new HashMap<>();


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == BLOCK_FOOD_SPOILAGE) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public void putItemStackPos(BlockPos blockPos, ItemStack itemStack) {
        blocks.computeIfAbsent(blockPos, pos -> new ArrayList<>()).add(itemStack);
    }

    @Override
    public void removeItemStackPos(BlockPos blockPos, ItemStack itemStack) {
        List<ItemStack> list = blocks.get(blockPos);

        if (list == null) {
            return;
        }

        list.remove(itemStack);

        if (list.isEmpty()) {
            blocks.remove(blockPos);
        }
    }

    @Override
    public ItemStack getLastItemStackByPos(BlockPos blockPos) {
        List<ItemStack> list = blocks.get(blockPos);

        if (list == null || list.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return list.get(list.size() - 1);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbtTag = new CompoundTag();
        ListTag listTag = new ListTag();

        for (Map.Entry<BlockPos, List<ItemStack>> entry : blocks.entrySet()) {
            BlockPos blockPos = entry.getKey();
            List<ItemStack> itemStacks = entry.getValue();

            for (ItemStack stack : itemStacks) {
                CompoundTag tag = new CompoundTag();
                tag.putLong("pos", blockPos.asLong());
                tag.put("stack", stack.save(new CompoundTag()));
                listTag.add(tag);
            }
        }

        nbtTag.put("blocks", listTag);
        return nbtTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        blocks.clear();
        if (nbt.contains("blocks")) {
            ListTag listTag = nbt.getList("blocks", Tag.TAG_COMPOUND);

            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag tag = listTag.getCompound(i);
                BlockPos blockPos = BlockPos.of(tag.getLong("pos"));
                ItemStack itemStack = ItemStack.of(tag.getCompound("stack"));
                putItemStackPos(blockPos, itemStack);
            }
        }
    }
}