package yuuria.stackupper.stackupper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yuuria.stackupper.stackupper.StackSupplier;

@Mixin(value = Slot.class, remap = false)
public class SlotMixin {
    @ModifyReturnValue(
            method = "getMaxStackSize()I",
            at = @At("RETURN")
    )
    private int fixSlotGetMaxStackSize(int orig) { return StackSupplier.getMaxStack(); }

    @ModifyReturnValue(
            method = "getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At("RETURN")
    )
    private int fixSlotGetMaxStackSizeIStack(int orig) { return StackSupplier.getMaxStack(); }
}
