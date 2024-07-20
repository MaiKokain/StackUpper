package yuria.stackupper.mixin.minecraft;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = net.minecraft.world.inventory.AnvilMenu.class, remap = false)
public class AnvilMenu {
    @Redirect(
            method = "onTake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 3
            )
    )
    void onTakeEnchantedBooks(Container input, int i, ItemStack itemStack)
    {
        input.getItem(1).shrink(1);
    }
}
