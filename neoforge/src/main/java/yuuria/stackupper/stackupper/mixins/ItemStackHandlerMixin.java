package yuuria.stackupper.stackupper.mixins;

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
    private int fixCapacity(int original)
    {
        return StackSupplier.getMaxStack();
    }
}
