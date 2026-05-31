package net.therealvoin.notjustspoiled.mixin.mods.alexscaves.accessor;

import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = NuclearFurnaceBlockEntity.class, remap = false)
public interface NuclearFurnaceBlockEntityAccessor {
    @Accessor int getCookTime();
}