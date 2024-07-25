package yuria.stackupper.mixin.minecraft.size;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuria.stackupper.config.StackSize;

@Mixin(value = ItemStack.class, remap = false)
public class ItemStackMixin {

    @Inject(
            method = "getMaxStackSize",
            at = @At("RETURN"),
            cancellable = true
    )
    private void setMaxStackSize(CallbackInfoReturnable<Integer> cir)
    {
        if (cir.getReturnValue() > 1) {
            cir.cancel();
            cir.setReturnValue(StackSize.getMaxStackSize());
        }
    }
}