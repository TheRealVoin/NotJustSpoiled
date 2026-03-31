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

import java.util.HashMap;
import java.util.Map;

public class BlockFoodSpoilageProvider implements IBlockFoodSpoilage, ICapabilitySerializable<CompoundTag> {
    public static final Capability<IBlockFoodSpoilage> BLOCK_FOOD_SPOILAGE = CapabilityManager.get(new CapabilityToken<>() {});
    private final LazyOptional<IBlockFoodSpoilage> optional = LazyOptional.of(() -> this);

    private final Map<BlockPos, ItemStack> blocks = new HashMap<>();

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == BLOCK_FOOD_SPOILAGE) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void putItemStackPos(BlockPos blockPos, ItemStack itemStack) {
        blocks.put(blockPos, itemStack);
    }

    @Override
    public void removeItemStackPos(BlockPos blockPos) {
        blocks.remove(blockPos);
    }

    @Override
    public ItemStack getItemStackByPos(BlockPos blockPos) {
        return blocks.get(blockPos);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbtTag = new CompoundTag();
        ListTag listTag = new ListTag();

        for (Map.Entry<BlockPos, ItemStack> entry : blocks.entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putLong("pos", entry.getKey().asLong());
            tag.put("stack", entry.getValue().save(new CompoundTag()));
            listTag.add(tag);
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