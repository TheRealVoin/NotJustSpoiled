package net.therealvoin.notjustspoiled.compat.mixin.dragnlivestock;

import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.therealvoin.notjustspoiled.common.util.SimpleContainerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractOMount.class)
public abstract class AbstractOMountMixin {
    @Shadow public abstract SimpleContainer getInventory();

    @Inject(method = "createInventory", at = @At("TAIL"))
    private void connectSimpleContainerWithHorse(CallbackInfo ci) {
        AbstractHorse horse = (AbstractHorse) (Object) this;
        ((SimpleContainerAccessor) this.getInventory()).setHorse(horse);
    }
}