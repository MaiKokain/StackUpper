package yuuria.stackupper.mixins.early;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yuuria.stackupper.StackUpperHelpers;

@Mixin(Slot.class)
public class SlotPatch {
    @ModifyReturnValue(method = {"getItemStackLimit", "getSlotStackLimit"}, at = @At("RETURN"))
    private int stackup$fixSlot(int original)
    {
        return StackUpperHelpers.getMaxStackSize();
    }
}
