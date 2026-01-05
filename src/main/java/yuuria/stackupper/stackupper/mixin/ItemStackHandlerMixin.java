//? if != 1.21.10 {
package yuuria.stackupper.stackupper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yuuria.stackupper.stackupper.StackSupplier;

@Mixin(value = ItemStackHandler.class, remap = false)
public class ItemStackHandlerMixin {
    @ModifyReturnValue(
            method = "getSlotLimit",
            at = @At("RETURN")
    )
    private int fixGetSlotLimit(int original)
    {
        return StackSupplier.getMaxStack();
    }
}
//?} else {
/*package yuuria.stackupper.stackupper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yuuria.stackupper.stackupper.StackSupplier;

@Mixin(value = ItemAccessItemHandler.class, remap = false)
public class ItemStackHandlerMixin {
    @ModifyReturnValue(
            method = "getCapacity(ILnet/neoforged/neoforge/transfer/item/ItemResource;)I",
            at = @At("RETURN")
    )
    private int fixCapacity(int orig)
    {
        return Integer.MAX_VALUE;
    }
}
*///?}