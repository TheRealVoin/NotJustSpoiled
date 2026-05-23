package net.therealvoin.notjustspoiled.mixin.mods;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.*;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = {
        "com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity",
        "com.github.alexthe666.alexsmobs.tileentity.TileEntityCapsid",
        "dev.enemeez.simplefarming.common.block.entity.FermenterBlockEntity",
        "toughasnails.block.entity.WaterPurifierBlockEntity"
}, remap = false)
public abstract class SetItemMixin {
    @Inject(method = {"setItem", "m_6836_"}, at = @At("TAIL"))
    private void changeFoodEnvironmentWhenPlacedInContainer(int index, ItemStack stackToSetInStorage, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToSetInStorage, FoodEnvironment.STORAGE, ((BlockEntity)(Object)this).getLevel());
    }
}