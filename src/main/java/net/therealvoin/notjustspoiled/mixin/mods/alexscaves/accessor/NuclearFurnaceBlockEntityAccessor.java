package net.therealvoin.notjustspoiled.mixin.mods.alexscaves.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(targets = "com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity", remap = false)
public interface NuclearFurnaceBlockEntityAccessor {
    @Accessor int getCookTime();
}