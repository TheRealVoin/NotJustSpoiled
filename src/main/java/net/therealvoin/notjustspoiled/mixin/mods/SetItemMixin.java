package net.therealvoin.notjustspoiled.mixin.mods;

import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityCapsid;
import dev.enemeez.simplefarming.common.block.entity.FermenterBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.core.foodspoilage.FoodSpoilageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toughasnails.block.entity.WaterPurifierBlockEntity;
import vectorwing.farmersdelight.common.block.entity.BasketBlockEntity;

@Mixin(value = {
        NuclearFurnaceBlockEntity.class,
        TileEntityCapsid.class,
        BasketBlockEntity.class,
        FermenterBlockEntity.class,
        WaterPurifierBlockEntity.class
}, remap = false)
public abstract class SetItemMixin {
    @Inject(method = {"setItem", "m_6836_"}, at = @At("TAIL"))
    private void changeFoodEnvironmentWhenPlacedInContainer(int index, ItemStack stackToSetInStorage, CallbackInfo ci) {
        FoodSpoilageManager.changeEnvironmentAndUpdate(stackToSetInStorage, FoodEnvironment.STORAGE, ((BlockEntity)(Object)this).getLevel());
    }
}