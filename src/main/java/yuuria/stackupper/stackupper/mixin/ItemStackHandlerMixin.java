package yuuria.stackupper.stackupper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yuuria.stackupper.stackupper.Constants;

@Mixin(value = ItemStackHandler.class, remap = false)
public class ItemStackHandlerMixin {
    @ModifyReturnValue(
            method = "getSlotLimit",
            at = @At("RETURN")
    )
    private int fixGetSlotLimit(int original)
    {
        return Constants.globalStackLimitSupplier.get();
    }
}
