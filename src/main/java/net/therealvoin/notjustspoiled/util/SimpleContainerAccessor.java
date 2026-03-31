package net.therealvoin.notjustspoiled.util;

import net.minecraft.world.entity.animal.horse.AbstractHorse;

public interface SimpleContainerAccessor {
    AbstractHorse getHorse();
    void setHorse(AbstractHorse horse);
}