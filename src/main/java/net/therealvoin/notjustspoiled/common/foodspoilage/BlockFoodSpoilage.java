package net.therealvoin.notjustspoiled.common.foodspoilage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockFoodSpoilage extends SavedData {
    private final Map<BlockPos, List<ItemStack>> blocks = new HashMap<>();

    public static BlockFoodSpoilage get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                BlockFoodSpoilage::load,
                BlockFoodSpoilage::new,
                "block_food_spoilage"
        );
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        ListTag listTag = new ListTag();

        for (Map.Entry<BlockPos, List<ItemStack>> entry : blocks.entrySet()) {
            BlockPos blockPos = entry.getKey();

            for (ItemStack itemStack : entry.getValue()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putLong("pos", blockPos.asLong());
                compoundTag.put("stack", itemStack.save(new CompoundTag()));
                listTag.add(compoundTag);
            }
        }

        tag.put("blocks", listTag);
        return tag;
    }

    public static BlockFoodSpoilage load(CompoundTag tag) {
        BlockFoodSpoilage data = new BlockFoodSpoilage();

        if (tag.contains("blocks")) {
            ListTag listTag = tag.getList("blocks", Tag.TAG_COMPOUND);

            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compoundTag = listTag.getCompound(i);
                BlockPos blockPos = BlockPos.of(compoundTag.getLong("pos"));
                ItemStack itemStack = ItemStack.of(compoundTag.getCompound("stack"));
                data.putItemStackPos(blockPos, itemStack);
            }
        }

        return data;
    }

    public ItemStack getLastItemStackByPos(BlockPos pos) {
        List<ItemStack> list = blocks.get(pos);

        if (list == null || list.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return list.get(list.size() - 1);
    }

    public void putItemStackPos(BlockPos pos, ItemStack stack) {
        blocks.computeIfAbsent(pos, blockPos -> new ArrayList<>()).add(stack);
        super.setDirty();
    }

    public void removeItemStackPos(BlockPos pos, ItemStack itemStack) {
        List<ItemStack> list = blocks.get(pos);

        if (list == null) {
            return;
        }

        list.remove(itemStack);

        if (list.isEmpty()) {
            blocks.remove(pos);
        }

        super.setDirty();
    }
}