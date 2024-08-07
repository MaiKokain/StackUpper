package yuria.stackupper.mixin.minecraft.size;

import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
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

    @ModifyArg(
            method = "lambda$static$3",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;"),
            index = 1
    )
    private static int lol(int p_270883_)
    {
        return StackSize.getMaxStackSize();
    }
}
