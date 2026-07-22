package net.therealvoin.notjustspoiled.mixin.minecraft;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.therealvoin.notjustspoiled.NotJustSpoiled;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodEnvironment;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilage;
import net.therealvoin.notjustspoiled.common.foodspoilage.FoodSpoilageManager;
import net.therealvoin.notjustspoiled.common.util.SimpleContainerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RandomizableContainerBlockEntity.class, ChestBoat.class, AbstractMinecartContainer.class, SimpleContainer.class, AbstractFurnaceBlockEntity.class})
public abstract class SetItemMixin {
    @Unique private static long notJustSpoiled$lastWarningMessageTime = 0;

    @Inject(method = "setItem", at = @At("TAIL"))
    private void changeFoodEnvironmentWhenPlacedInContainer(int index, ItemStack itemStack, CallbackInfo ci) {
        Object object = this;
        Level level = null;

        if (object instanceof AbstractMinecartContainer minecartContainer) {
            level = minecartContainer.level();
        } else if (object instanceof BaseContainerBlockEntity blockEntity) {
            level = blockEntity.getLevel();
        } else if (object instanceof SimpleContainer simpleContainer) {
            AbstractHorse horse = ((SimpleContainerAccessor) simpleContainer).getHorse();
            if (horse != null) {
                level = horse.level();
            }
        } else if (object instanceof ChestBoat chestBoat) {
            level = chestBoat.level();
        }

        if (level == null) {
            return;
        }

        FoodSpoilageManager.changeEnvironmentAndUpdate(itemStack, FoodEnvironment.STORAGE, level);
        notJustSpoiled$sendWarningMessageIfNeeded(itemStack, level);
    }

    @Unique
    private static void notJustSpoiled$sendWarningMessageIfNeeded(ItemStack itemStack, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        FoodSpoilage foodSpoilage = FoodSpoilage.of(itemStack);

        if (foodSpoilage == null) {
            return;
        }

        if (foodSpoilage.getFoodLifetime() == 0) {
            long currentGameTime = serverLevel.getGameTime();
            if (currentGameTime == notJustSpoiled$lastWarningMessageTime) {
                return;
            }

            Component modName = Component.literal(NotJustSpoiled.MOD_NAME).withStyle(ChatFormatting.BOLD);
            Component issuesPage = Component.translatable("message.notjustspoiled.warning_message.issues_page").withStyle(Style.EMPTY.withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/TheRealVoin/NotJustSpoiled/issues")));

            serverLevel.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("message.notjustspoiled.warning_message", modName, issuesPage), false);
            notJustSpoiled$lastWarningMessageTime = currentGameTime;
        }
    }
}