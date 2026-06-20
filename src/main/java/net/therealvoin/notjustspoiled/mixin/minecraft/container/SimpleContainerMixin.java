package net.therealvoin.notjustspoiled.mixin.minecraft.container;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.therealvoin.notjustspoiled.common.util.SimpleContainerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SimpleContainer.class)
public abstract class SimpleContainerMixin implements SimpleContainerAccessor {
    @Unique AbstractHorse horse;

    @Override
    public AbstractHorse getHorse() {
        return this.horse;
    }

    @Override
    public void setHorse(AbstractHorse horse) {
        this.horse = horse;
    }
}