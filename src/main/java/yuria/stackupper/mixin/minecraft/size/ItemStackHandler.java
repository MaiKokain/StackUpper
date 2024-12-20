package yuria.stackupper.mixin.minecraft.size;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = net.neoforged.neoforge.items.ItemStackHandler.class, remap = false)
public class ItemStackHandler {
    @ModifyReturnValue(
            method = "getSlotLimit",
            at = @At("RETURN")
    )
    public int getSlotLimit(int val)
    {
        return Integer.MAX_VALUE;
    }
}
