package yuuria.stackupper.stackupper.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AnvilMenu.class, remap = false)
public class AnvilMenuMixin {
    @WrapOperation(
            method = "onTake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
                    //? if = 1.21 {
                    ordinal = 3
                    //?} else
                    /*ordinal = 2*/
            )
    )
    void onTakeSetItem(Container instance, int i, ItemStack itemStack, Operation<Void> original)
    {
        instance.getItem(1).shrink(1);
    }
}
