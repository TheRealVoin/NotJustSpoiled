package net.therealvoin.notjustspoiled.mixin.minecraft.entity;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.therealvoin.notjustspoiled.common.util.SimpleContainerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin {
    @Shadow protected SimpleContainer inventory;

    @Inject(method = "createInventory", at = @At("TAIL"))
    private void connectSimpleContainerWithHorse(CallbackInfo ci) {
        AbstractHorse horse = (AbstractHorse) (Object) this;
        ((SimpleContainerAccessor) inventory).setHorse(horse);
    }
}